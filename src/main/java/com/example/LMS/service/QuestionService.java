package com.example.LMS.service;

import com.example.LMS.entity.Assessment;
import com.example.LMS.entity.Question;
import com.example.LMS.entity.User;
import com.example.LMS.repository.AssessmentRepository;
import com.example.LMS.repository.QuestionRepository;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final AssessmentRepository assessmentRepository;

    public QuestionService(QuestionRepository questionRepository, AssessmentRepository assessmentRepository) {
        this.questionRepository = questionRepository;
        this.assessmentRepository = assessmentRepository;
    }

    public Question addQuestionToQuiz(Long quizId, Question questionRequest, User instructor) {
        // Retrieve the quiz
        Assessment quiz = assessmentRepository.findById(quizId)
                .orElseThrow(() -> new IllegalArgumentException("Quiz not found"));

        // Ensure the instructor is authorized to add questions to the quiz
        if (!Objects.equals(quiz.getCourse().getInstructor().getId(), instructor.getId())) {
            throw new IllegalArgumentException("You are not authorized to add questions to this quiz.");
        }

        // Ensure the assessment is a quiz
        if (!quiz.getType().equals(Assessment.Type.QUIZ)) {
            throw new IllegalArgumentException("This assessment is not a quiz.");
        }

        // Associate the question with the quiz and save it
        questionRequest.setAssessment(quiz);
        return questionRepository.save(questionRequest);
    }
}

