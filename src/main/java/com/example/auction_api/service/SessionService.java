package com.example.auction_api.service;

import com.example.auction_api.model.UserSession;
import com.example.auction_api.repository.SessionRepository;
import com.example.auction_api.security.JwtService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
public class SessionService {

    private final SessionRepository sessionRepository;
    private final JwtService jwtService;

    public SessionService(SessionRepository sessionRepository, JwtService jwtService) {
        this.sessionRepository = sessionRepository;
        this.jwtService = jwtService;
    }

    @Transactional
    public UserSession createSession(String username, String deviceId) {
        String accessJti = UUID.randomUUID().toString();
        String refreshJti = UUID.randomUUID().toString();

        String accessToken = jwtService.generateAccessToken(username, accessJti);
        String refreshToken = jwtService.generateRefreshToken(username, refreshJti);

        UserSession session = new UserSession();
        session.setUsername(username);
        session.setDeviceId(deviceId);
        session.setAccessToken(accessToken);
        session.setRefreshToken(refreshToken);
        
        session.setAccessTokenExpiry(jwtService.getExpirationFromToken(accessToken).toInstant());
        session.setRefreshTokenExpiry(jwtService.getExpirationFromToken(refreshToken).toInstant());
        session.setStatus("ACTIVE"); // Используем строку вместо Enum

        return sessionRepository.save(session);
    }

    @Transactional
    public UserSession refreshSession(String refreshToken) {
        UserSession oldSession = sessionRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new IllegalArgumentException("Invalid refresh token"));

        if (!"ACTIVE".equals(oldSession.getStatus())) {
            oldSession.setStatus("REVOKED"); // Используем строку
            sessionRepository.save(oldSession);
            throw new IllegalStateException("Refresh token already used or revoked");
        }

        if (oldSession.getRefreshTokenExpiry().isBefore(Instant.now())) {
            throw new IllegalStateException("Refresh token expired");
        }

        oldSession.setStatus("USED"); // Используем строку
        sessionRepository.save(oldSession);

        return createSession(oldSession.getUsername(), oldSession.getDeviceId());
    }

    @Transactional
    public void revokeSession(String refreshToken) {
        UserSession session = sessionRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new IllegalArgumentException("Invalid refresh token"));
        session.setStatus("REVOKED"); // Используем строку
        sessionRepository.save(session);
    }
}