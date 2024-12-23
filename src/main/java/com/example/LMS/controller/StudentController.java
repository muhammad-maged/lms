package com.example.LMS.controller;

import com.example.LMS.entity.Course;
import com.example.LMS.entity.User;
import com.example.LMS.security.CustomUserDetails;
import com.example.LMS.service.EnrollmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/student")
public class StudentController {

    private final EnrollmentService enrollmentService;

    public StudentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @GetMapping("/courses")
    public ResponseEntity<List<Course>> getAvailableCourses() {
        List<Course> courses = enrollmentService.getAvailableCourses();
        return ResponseEntity.ok(courses);
    }

    @PostMapping("/enroll")
    public ResponseEntity<String> enroll(@RequestParam Long courseId,   @AuthenticationPrincipal CustomUserDetails userDetails) {

        User student = userDetails.getUser(); // Get the full User entity
        enrollmentService.enrollStudent(student.getId(), courseId);
        return ResponseEntity.ok("Enrollment successful");
    }
}
