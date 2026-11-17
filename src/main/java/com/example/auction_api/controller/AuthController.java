package com.example.auction_api.controller;

import com.example.auction_api.dto.AuthRequest;
import com.example.auction_api.model.User;
import com.example.auction_api.model.UserSession;
import com.example.auction_api.repository.UserRepository;
import com.example.auction_api.service.SessionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final SessionService sessionService;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, SessionService sessionService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.sessionService = sessionService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        if (request.getUsername() == null || request.getPassword() == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Username and password are required"));
        }

        User user = userRepository.findAll().stream()
                .filter(u -> u.getUsername().equals(request.getUsername()))
                .findFirst()
                .orElse(null);

        if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid credentials"));
        }

        UserSession session = sessionService.createSession(user.getUsername(), request.getDeviceId());
        
        return ResponseEntity.ok(Map.of(
                "accessToken", session.getAccessToken(),
                "refreshToken", session.getRefreshToken()
        ));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AuthRequest request) {
        if (request.getUsername() == null || request.getPassword() == null || request.getPassword().length() < 6) {
            return ResponseEntity.badRequest().body(Map.of("error", "Password must be at least 6 characters"));
        }

        boolean exists = userRepository.findAll().stream()
                .anyMatch(u -> u.getUsername().equals(request.getUsername()));
        if (exists) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", "Username already taken"));
        }

        User user = new User(
                request.getUsername(),
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                "USER"
        );
        userRepository.save(user);

        UserSession session = sessionService.createSession(user.getUsername(), request.getDeviceId());

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "accessToken", session.getAccessToken(),
                "refreshToken", session.getRefreshToken()
        ));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");
        if (refreshToken == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "refreshToken is required"));
        }

        try {
            UserSession newSession = sessionService.refreshSession(refreshToken);
            return ResponseEntity.ok(Map.of(
                    "accessToken", newSession.getAccessToken(),
                    "refreshToken", newSession.getRefreshToken()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", e.getMessage()));
        }
    }
}