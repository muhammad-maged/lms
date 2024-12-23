package com.example.LMS.controller;

import com.example.LMS.dto.CourseRequest;
import com.example.LMS.entity.*;
import com.example.LMS.service.*;
import com.example.LMS.security.CustomUserDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

import jakarta.validation.Valid;

import java.util.List;


@RestController
@RequestMapping("/api/instructor")
public class InstructorController {

    private final CourseService courseService;
    private final EnrollmentService enrollmentService;
    private final LessonService lessonService;
    private final AssessmentService assessmentService;
    private final QuestionService questionService;
    private final ProgressService progressService;


    public InstructorController(CourseService courseService, EnrollmentService enrollmentService,
                                LessonService lessonService, AssessmentService assessmentService,
                                AssessmentService assessmentService1, QuestionService questionService, ProgressService progressService) {
        this.courseService = courseService;
        this.enrollmentService = enrollmentService;
        this.lessonService = lessonService;
        this.assessmentService = assessmentService1;
        this.questionService = questionService;
        this.progressService = progressService;
    }

    @PreAuthorize("hasRole('INSTRUCTOR')")
    @PostMapping(value = "/courses", consumes = "multipart/form-data")
    public ResponseEntity<Course> createCourse(
            @Valid @ModelAttribute CourseRequest courseRequest,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        User instructor = userDetails.getUser(); // Get the full User entity

        Course createdCourse = courseService.createCourse(courseRequest, instructor);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCourse);
    }

    @PostMapping("/courses/{courseId}/lessons")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<Lesson> addLesson(
            @PathVariable Long courseId,
            @Valid @RequestBody Lesson lesson) {
        Lesson createdLesson = courseService.addLessonToCourse(courseId, lesson);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdLesson);
    }

    @GetMapping("/courses/{courseId}/students")
    @PreAuthorize("hasRole('INSTRUCTOR') or hasRole('ADMIN')")
    public ResponseEntity<List<User>> getEnrolledStudents(@PathVariable Long courseId) {
        List<User> students = enrollmentService.getEnrolledStudents(courseId);
        return ResponseEntity.ok(students);
    }


    @PostMapping("/lessons/{lessonId}/generate-otp")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<String> generateOtp(@PathVariable Long lessonId, @AuthenticationPrincipal CustomUserDetails userDetails)
    {
        User instructor = userDetails.getUser(); // Get the full User entity
        String otp = lessonService.generateOtp(lessonId, instructor);

        return ResponseEntity.status(HttpStatus.CREATED).body("OTP generated: " + otp);

    }

    @PostMapping("/courses/{courseId}/quizzes")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<Assessment> createQuiz(
            @PathVariable Long courseId,
            @RequestBody Assessment assessment) {
        assessment.setType(Assessment.Type.QUIZ);

        Assessment createdQuiz = assessmentService.createAssessment(courseId, assessment);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdQuiz);
    }

    @PostMapping("/quizzes/{quizId}/questions")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<Question> addQuestionToQuiz(
            @PathVariable Long quizId,
            @RequestBody Question questionRequest,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        User instructor = userDetails.getUser(); // Get the full User entity
        Question createdQuestion = questionService.addQuestionToQuiz(quizId, questionRequest, instructor);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdQuestion);
    }

    @GetMapping("/courses/{courseId}/progress")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<List<Map<String, Object>>> getProgress(
            @PathVariable Long courseId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        User instructor = userDetails.getUser(); // Get the full User entity
        List<Map<String, Object>> progressData = progressService.getProgressData(courseId, instructor);
        return ResponseEntity.ok(progressData);
    }

    @GetMapping(value = "/courses/{courseId}/progress/excel", produces = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<byte[]> downloadProgressExcel(
            @PathVariable Long courseId,
            @AuthenticationPrincipal CustomUserDetails userDetails) throws IOException {

        User instructor = userDetails.getUser(); // Get the full User entity
        byte[] excelData = progressService.generateProgressExcel(courseId, instructor);
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=progress.xlsx")
                .body(excelData);
    }

}
