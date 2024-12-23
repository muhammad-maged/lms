package com.example.LMS.service;

import com.example.LMS.entity.Lesson;
import com.example.LMS.entity.User;
import com.example.LMS.repository.LessonRepository;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class LessonService {

    private final LessonRepository lessonRepository;

    public LessonService(LessonRepository lessonRepository) {
        this.lessonRepository = lessonRepository;
    }

    public String generateOtp(Long lessonId, User instructor) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new IllegalArgumentException("Lesson not found"));

        if (!Objects.equals(lesson.getCourse().getInstructor().getId(), instructor.getId())) {
            throw new IllegalArgumentException("You are not authorized to generate OTP for this lesson");
        }

        String otp = String.valueOf((int) (Math.random() * 9000) + 1000); // Generate a 4-digit OTP
        lesson.setOtpCode(otp);
        lessonRepository.save(lesson);

        return otp;
    }
}
