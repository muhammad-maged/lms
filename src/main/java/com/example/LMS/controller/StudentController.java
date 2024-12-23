package com.example.LMS.controller;

import com.example.LMS.entity.Course;
import com.example.LMS.entity.User;
import com.example.LMS.security.CustomUserDetails;
import com.example.LMS.service.AttendanceService;
import com.example.LMS.service.EnrollmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/student")
public class StudentController {

    private final EnrollmentService enrollmentService;

    private final AttendanceService attendanceService;

    public StudentController(EnrollmentService enrollmentService, AttendanceService attendanceService) {

        this.enrollmentService = enrollmentService;
        this.attendanceService = attendanceService;
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

    @PostMapping("/lessons/{lessonId}/mark-attendance")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<String> markAttendance(
            @PathVariable Long lessonId,
            @RequestParam String otp,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        
        User student = userDetails.getUser(); // Get the full User entity
        attendanceService.markAttendance(student.getId(), lessonId, otp);
        return ResponseEntity.ok("Attendance marked successfully");
    }

}
