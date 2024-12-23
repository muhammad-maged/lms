package com.example.LMS.service;

import com.example.LMS.dto.CourseRequest;
import com.example.LMS.entity.Course;
import com.example.LMS.entity.User;
import com.example.LMS.repository.CourseRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class CourseService {

    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public Course createCourse(CourseRequest courseRequest, User instructor) {
        Course course = new Course();
        course.setTitle(courseRequest.getTitle());
        course.setDescription(courseRequest.getDescription());
        course.setDuration(courseRequest.getDuration());
        course.setInstructor(instructor);

        // Handle media file upload
        if (courseRequest.getMediaFile() != null && !courseRequest.getMediaFile().isEmpty()) {
            String mediaPath = saveMediaFile(courseRequest.getMediaFile());
            course.setMediaPath(mediaPath);
        }

        return courseRepository.save(course);
    }

    private String saveMediaFile(MultipartFile mediaFile) {
        try {
            // Define file storage path
            String filePath = "uploads/" + mediaFile.getOriginalFilename();
            Path path = Paths.get(filePath);

            // Save file to the directory
            Files.createDirectories(path.getParent());
            Files.write(path, mediaFile.getBytes());

            return filePath;
        } catch (IOException e) {
            throw new RuntimeException("Failed to save media file: " + e.getMessage());
        }
    }

}
