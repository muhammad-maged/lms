package com.example.LMS.service;

import com.example.LMS.entity.Assessment;
import com.example.LMS.entity.Question;
import com.example.LMS.entity.Submission;
import com.example.LMS.entity.User;
import com.example.LMS.repository.AssessmentRepository;
import com.example.LMS.repository.EnrollmentRepository;
import com.example.LMS.repository.QuestionRepository;
import com.example.LMS.repository.SubmissionRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SubmissionService {

    private final AssessmentRepository assessmentRepository;
    private final QuestionRepository questionRepository;
    private final SubmissionRepository submissionRepository;
    private final EnrollmentRepository enrollmentRepository;

    public SubmissionService(AssessmentRepository assessmentRepository,
                             QuestionRepository questionRepository,
                             SubmissionRepository submissionRepository,
                             EnrollmentRepository enrollmentRepository) {
        this.assessmentRepository = assessmentRepository;
        this.questionRepository = questionRepository;
        this.submissionRepository = submissionRepository;
        this.enrollmentRepository = enrollmentRepository;
    }

    public Map<String, Object> submitQuiz(Long quizId, Map<Long, String> answers, User student) throws JsonProcessingException {
        // Validate the quiz
        Assessment quiz = assessmentRepository.findById(quizId)
                .orElseThrow(() -> new IllegalArgumentException("Quiz not found"));

        if (!quiz.getType().equals(Assessment.Type.QUIZ)) {
            throw new IllegalArgumentException("This assessment is not a quiz.");
        }

/*
        if (!enrollmentRepository.existsByStudentIdAndCourseId(student.getId(), quiz.getCourse().getId())) {
            throw new IllegalArgumentException("You are not enrolled in this course.");
        }*/

        // Validate answers and calculate score
        List<Question> questions = questionRepository.findByAssessmentId(quizId);
        double score = 0;
        Map<String, String> feedback = new HashMap<>();

        for (Question question : questions) {
            String submittedAnswer = answers.get(question.getId());
            if (submittedAnswer == null) {
                feedback.put(question.getQuestion(), "No Answer");
            } else if (submittedAnswer.equalsIgnoreCase(question.getCorrectAnswer())) {
                score++;
                feedback.put(question.getQuestion(), "Correct");
            } else {
                feedback.put(question.getQuestion(), "Incorrect");
            }
        }

        // Save the submission
        Submission submission = new Submission();
        submission.setAssessment(quiz);
        submission.setStudent(student);
        submission.setAnswers(new ObjectMapper().writeValueAsString(answers));
        submission.setScore(score);
        submission.setFeedback(new ObjectMapper().writeValueAsString(feedback));
        submissionRepository.save(submission);

        // Return feedback
        Map<String, Object> result = new HashMap<>();
        result.put("score", score);
        result.put("feedback", feedback);
        return result;
    }

    public Map<String, Object> getQuizGrade(Long quizId, User student) throws IOException {
        // Validate the quiz
        Assessment quiz = assessmentRepository.findById(quizId)
                .orElseThrow(() -> new IllegalArgumentException("Quiz not found"));

        // Ensure the student is enrolled in the course
/*        if (!enrollmentRepository.existsByStudentIdAndCourseId(student.getId(), quiz.getCourse().getId())) {
            throw new IllegalArgumentException("You are not enrolled in this course.");
        }*/

        // Retrieve the student's submission
        Submission submission = submissionRepository.findByQuizIdAndStudentId(quizId, student.getId())
                .orElseThrow(() -> new IllegalArgumentException("You have not submitted this quiz."));

        // Prepare the response
        Map<String, Object> result = new HashMap<>();
        result.put("score", submission.getScore());
        result.put("feedback", new ObjectMapper().readValue(submission.getFeedback(), Map.class)); // Convert JSON feedback to a map
        return result;
    }
}
