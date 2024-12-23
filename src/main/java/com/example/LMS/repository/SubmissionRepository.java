package com.example.LMS.repository;

import com.example.LMS.entity.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubmissionRepository extends JpaRepository<Submission, Long> {

    @Query("SELECT s FROM Submission s WHERE s.assessment.id = :quizId AND s.student.id = :studentId")
    Optional<Submission> findByQuizIdAndStudentId(@Param("quizId") Long quizId, @Param("studentId") Long studentId);
}

