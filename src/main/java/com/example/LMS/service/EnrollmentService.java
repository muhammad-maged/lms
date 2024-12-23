package com.example.LMS.service;

import com.example.LMS.entity.Course;
import com.example.LMS.entity.Enrollment;
import com.example.LMS.entity.User;
import com.example.LMS.repository.CourseRepository;
import com.example.LMS.repository.EnrollmentRepository;
import com.example.LMS.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    public EnrollmentService(EnrollmentRepository enrollmentRepository, CourseRepository courseRepository, UserRepository userRepository) {
        this.enrollmentRepository = enrollmentRepository;
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
    }

    public List<Course> getAvailableCourses() {
        return courseRepository.findAll();
    }

    public Enrollment enrollStudent(Long studentId, Long courseId) {
        if (enrollmentRepository.existsByStudentIdAndCourseId(studentId, courseId)) {
            throw new IllegalArgumentException("Student is already enrolled in this course.");
        }

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found"));

        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));

        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setCourse(course);

        return enrollmentRepository.save(enrollment);
    }

    public List<User> getEnrolledStudents(Long courseId) {
        List<Enrollment> enrollments = enrollmentRepository.findByCourseId(courseId);
        return enrollments.stream().map(Enrollment::getStudent).toList();
    }
}
