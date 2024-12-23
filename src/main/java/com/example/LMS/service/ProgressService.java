package com.example.LMS.service;

import com.example.LMS.entity.Assessment;
import com.example.LMS.entity.User;
import com.example.LMS.repository.AttendanceRepository;
import com.example.LMS.repository.CourseRepository;
import com.example.LMS.repository.EnrollmentRepository;
import com.example.LMS.repository.SubmissionRepository;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Service;
import com.example.LMS.entity.Course;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


@Service
public class ProgressService {

    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final SubmissionRepository submissionRepository;
    private final AttendanceRepository attendanceRepository;

    public ProgressService(CourseRepository courseRepository,
                           EnrollmentRepository enrollmentRepository,
                           SubmissionRepository submissionRepository,
                           AttendanceRepository attendanceRepository) {
        this.courseRepository = courseRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.submissionRepository = submissionRepository;
        this.attendanceRepository = attendanceRepository;
    }

    public List<Map<String, Object>> getProgressData(Long courseId, User instructor) {
        // Validate the course and instructor
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found"));

        if (!Objects.equals(course.getInstructor().getId(), instructor.getId())) {
            throw new IllegalArgumentException("You are not authorized to view progress for this course.");
        }

        // Get enrolled students
        List<User> students = enrollmentRepository.findStudentsByCourseId(courseId);

        // Prepare progress data
        List<Map<String, Object>> progressData = new ArrayList<>();
        for (User student : students) {
            Map<String, Object> studentProgress = new HashMap<>();
            studentProgress.put("student", student);

            // Quiz scores
            double averageQuizScore = submissionRepository.findAverageScoreByStudentAndCourse(student.getId(), courseId);
            studentProgress.put("averageQuizScore", averageQuizScore);

            // Assignment submissions
            long totalAssignments = submissionRepository.countByStudentAndCourseAndType(student.getId(), courseId, Assessment.Type.ASSIGNMENT);
            studentProgress.put("assignmentsSubmitted", totalAssignments);

            // Attendance
            long attendanceCount = attendanceRepository.countByStudentAndCourse(student.getId(), courseId);
            studentProgress.put("attendanceCount", attendanceCount);

            progressData.add(studentProgress);
        }

        return progressData;
    }

    public byte[] generateProgressExcel(Long courseId, User instructor) throws IOException {
        List<Map<String, Object>> progressData = getProgressData(courseId, instructor);

        // Create an Excel workbook and sheet
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Progress");

        // Add headers
        Row headerRow = sheet.createRow(0);
        String[] headers = {"Student ID", "Student Name", "Average Quiz Score", "Assignments Submitted", "Attendance Count"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }

        // Add data rows
        int rowNum = 1;
        for (Map<String, Object> data : progressData) {
            Row row = sheet.createRow(rowNum++);
            User student = (User) data.get("student");
            row.createCell(0).setCellValue(student.getId());
            row.createCell(1).setCellValue(student.getUsername());
            row.createCell(2).setCellValue((double) data.get("averageQuizScore"));
            row.createCell(3).setCellValue((long) data.get("assignmentsSubmitted"));
            row.createCell(4).setCellValue((long) data.get("attendanceCount"));
        }

        // Write to a byte array
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();

        return outputStream.toByteArray();
    }
}
