package com.example.LMS.service;

import com.example.LMS.entity.Assessment;
import com.example.LMS.entity.Course;
import com.example.LMS.repository.AssessmentRepository;
import com.example.LMS.repository.CourseRepository;
import org.springframework.stereotype.Service;

@Service
public class AssessmentService {

    private final AssessmentRepository assessmentRepository;
    private final CourseRepository courseRepository;

    public AssessmentService(AssessmentRepository assessmentRepository, CourseRepository courseRepository) {
        this.assessmentRepository = assessmentRepository;
        this.courseRepository = courseRepository;
    }

    public Assessment createAssessment(Long courseId, Assessment assessment) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found"));
        assessment.setCourse(course);
        return assessmentRepository.save(assessment);
    }
}
