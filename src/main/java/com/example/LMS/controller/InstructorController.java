package com.example.LMS.controller;

import com.example.LMS.dto.CourseRequest;
import com.example.LMS.entity.Course;
import com.example.LMS.entity.User;
import com.example.LMS.service.CourseService;
import com.example.LMS.security.CustomUserDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/instructor")
public class InstructorController {

    private final CourseService courseService;

    public InstructorController(CourseService courseService) {
        this.courseService = courseService;
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
}
