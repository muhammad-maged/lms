package com.example.LMS;

import com.example.LMS.entity.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import com.example.LMS.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class LmsApplication {

	public static void main(String[] args) {
		SpringApplication.run(LmsApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(UserRepository userRepository) {
		return args -> {
			// Create a new user
			User user = new User();
			user.setUsername("testuser");
			user.setPassword("testpassword");
			user.setRole(User.Role.STUDENT);
			// Save the user to the database
			userRepository.save(user);
			// Print the saved user
			System.out.println("User created: " + user);
		};
	}

}
