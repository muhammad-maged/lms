package com.example.LMS.service;

import com.example.LMS.dto.CreateUserRequest;
import com.example.LMS.entity.User;
import com.example.LMS.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User createUser(CreateUserRequest request) {
        // Check if username already exists
        Optional<User> existingUser = userRepository.findByUsername(request.getUsername());
        if (existingUser.isPresent()) {
            // retunn the error as json
            throw new IllegalArgumentException("Username already exists: " + request.getUsername());
        }

        // Validate role
        if (!isValidRole(request.getRole())) {
            throw new IllegalArgumentException("Invalid role: " + request.getRole());
        }

        // Create a new User entity
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword())); // Encrypt password
        user.setRole(User.Role.valueOf(request.getRole().toUpperCase()));

        // Save the user to the database
        return userRepository.save(user);
    }

    private boolean isValidRole(String role) {
        try {
            User.Role.valueOf(role.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
