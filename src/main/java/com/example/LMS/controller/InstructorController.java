package com.example.LMS.controller;

import com.example.LMS.dto.CourseRequest;
import com.example.LMS.entity.Course;
import com.example.LMS.entity.Lesson;
import com.example.LMS.entity.User;
import com.example.LMS.service.CourseService;
import com.example.LMS.security.CustomUserDetails;
import com.example.LMS.service.EnrollmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.List;


@RestController
@RequestMapping("/api/instructor")
public class InstructorController {

    private final CourseService courseService;
    private final EnrollmentService enrollmentService;


    public InstructorController(CourseService courseService, EnrollmentService enrollmentService) {
        this.courseService = courseService;
        this.enrollmentService = enrollmentService;
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
}


