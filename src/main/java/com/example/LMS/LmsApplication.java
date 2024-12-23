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

/*	@Bean
	CommandLineRunner commandLineRunner(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		return args -> {
			// Check if an admin user already exists
			if (userRepository.findByUsername("admin").isEmpty()) {
				// Create a default admin user
				User adminUser = new User();
				adminUser.setUsername("admin");
				adminUser.setPassword(passwordEncoder.encode("admin123")); // Encrypt password
				adminUser.setRole(User.Role.ADMIN);
				userRepository.save(adminUser);

				System.out.println("Default admin user created: username='admin', password='admin123'");
			}
		};
	}*/

}
