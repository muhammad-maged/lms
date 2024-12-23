package com.example.LMS.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "submissions")
public class Submission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "assessment_id", nullable = false)
    private Assessment assessment;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    @Column(name = "answers", nullable = false)
    private String answers; // JSON format: {"1": "A", "2": "True"}

    @Column(name = "score", nullable = false)
    private double score;

    @Column(name = "feedback", nullable = false)
    private String feedback; // JSON format: {"1": "Correct", "2": "Incorrect"}

    @Column(name = "submitted_at", nullable = false)
    private LocalDateTime submittedAt = LocalDateTime.now();

    public void setAssessment(Assessment quiz) {
        this.assessment = quiz;
    }

    public void setStudent(User student) {
        this.student = student;
    }

    public void setAnswers(String s) {
        this.answers = s;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public void setFeedback(String s) {
        this.feedback = s;
    }
}
