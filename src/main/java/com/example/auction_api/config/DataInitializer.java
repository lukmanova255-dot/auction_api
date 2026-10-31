package com.example.auction_api.config;

import com.example.auction_api.model.User;
import com.example.auction_api.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initAdmin(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            boolean adminExists = userRepository.findAll().stream()
                    .anyMatch(u -> "admin".equals(u.getUsername()));
            if (!adminExists) {
                User admin = new User(
                        "admin",
                        "admin@example.com",
                        passwordEncoder.encode("admin123"),
                        "ADMIN"
                );
                userRepository.save(admin);
                System.out.println(">>> Admin created: username=admin, password=admin123");
            } else {
                System.out.println(">>> Admin already exists");
            }
        };
    }
}