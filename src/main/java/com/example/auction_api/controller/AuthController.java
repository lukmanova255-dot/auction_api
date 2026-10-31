package com.example.auction_api.controller;

import com.example.auction_api.dto.AuthRequest;
import com.example.auction_api.model.User;
import com.example.auction_api.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Возвращаем CSRF-токен в JSON и СОЗДАЁМ сессию (JSESSIONID)
    @GetMapping("/csrf")
    public Map<String, String> getCsrf(HttpServletRequest request, HttpSession session) {
        CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        if (csrfToken == null) {
            csrfToken = (CsrfToken) request.getAttribute("_csrf");
        }
        return Map.of("csrfToken", csrfToken != null ? csrfToken.getToken() : "NOT_FOUND");
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, Object> register(@RequestBody AuthRequest req) {
        if (req.getPassword() == null || req.getPassword().length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters");
        }
        boolean exists = userRepository.findAll().stream()
                .anyMatch(u -> u.getUsername().equals(req.getUsername()));
        if (exists) {
            throw new IllegalStateException("Username already taken");
        }
        User user = new User(
                req.getUsername(),
                req.getEmail(),
                passwordEncoder.encode(req.getPassword()),
                "USER"
        );
        userRepository.save(user);
        return Map.of(
                "id", user.getId(),
                "username", user.getUsername(),
                "role", user.getRole()
        );
    }
}