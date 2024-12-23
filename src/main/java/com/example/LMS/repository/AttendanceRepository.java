package com.example.LMS.repository;

import com.example.LMS.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    boolean existsByStudentIdAndLessonId(Long studentId, Long lessonId);
}
