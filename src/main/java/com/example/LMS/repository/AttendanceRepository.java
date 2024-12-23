package com.example.LMS.repository;

import com.example.LMS.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    boolean existsByStudentIdAndLessonId(Long studentId, Long lessonId);

    @Query("SELECT COUNT(a) FROM Attendance a " +
            "WHERE a.student.id = :studentId AND a.lesson.course.id = :courseId")
    long countByStudentAndCourse(@Param("studentId") Long studentId, @Param("courseId") Long courseId);
}
