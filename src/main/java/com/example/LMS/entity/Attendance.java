package com.example.LMS.entity;

import com.example.LMS.entity.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "attendance")
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    @ManyToOne
    @JoinColumn(name = "lesson_id", nullable = false)
    private Lesson lesson;

    @Column(name = "marked_at", nullable = false)
    private LocalDateTime markedAt = LocalDateTime.now();

   // setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setStudent(User student) {
        this.student = student;
    }

    public void setLesson(Lesson lesson) {
        this.lesson = lesson;
    }

    public void setMarkedAt(LocalDateTime markedAt) {
        this.markedAt = markedAt;
    }

    // getters
    public Long getId() {
        return id;
    }

    public User getStudent() {
        return student;
    }

    public Lesson getLesson() {
        return lesson;
    }

    public LocalDateTime getMarkedAt() {
        return markedAt;
    }

}
