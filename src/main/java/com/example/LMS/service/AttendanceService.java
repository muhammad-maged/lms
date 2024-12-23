package com.example.LMS.service;

import com.example.LMS.entity.Attendance;
import com.example.LMS.entity.Lesson;
import com.example.LMS.entity.User;
import com.example.LMS.repository.AttendanceRepository;
import com.example.LMS.repository.EnrollmentRepository;
import com.example.LMS.repository.LessonRepository;
import org.springframework.stereotype.Service;

@Service
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final LessonRepository lessonRepository;
    private final EnrollmentRepository enrollmentRepository;

    public AttendanceService(AttendanceRepository attendanceRepository,
                             LessonRepository lessonRepository,
                             EnrollmentRepository enrollmentRepository) {
        this.attendanceRepository = attendanceRepository;
        this.lessonRepository = lessonRepository;
        this.enrollmentRepository = enrollmentRepository;
    }

    public void markAttendance(Long studentId, Long lessonId, String otp) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new IllegalArgumentException("Lesson not found"));

        if (!otp.equals(lesson.getOtpCode())) {
            throw new IllegalArgumentException("Invalid OTP");
        }

        if (attendanceRepository.existsByStudentIdAndLessonId(studentId, lessonId)) {
            throw new IllegalArgumentException("Attendance already marked for this lesson");
        }

        if (!enrollmentRepository.existsByStudentIdAndCourseId(studentId, lesson.getCourse().getId())) {
            throw new IllegalArgumentException("Student is not enrolled in this course");
        }

        Attendance attendance = new Attendance();
        User student = new User();
        // get the student from the database using the studentId
        student.setId(studentId);
        attendance.setStudent(student);
        attendance.setLesson(lesson);
        attendanceRepository.save(attendance);
    }
}
