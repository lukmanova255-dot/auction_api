package com.example.auction_api.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

@Service
public class JwtService {

    // Секретный ключ для подписи HS256 (не менее 32 символов)
    private static final String SECRET_STRING = "my-super-secret-key-for-jwt-1bac24020-auction";
    private final SecretKey secretKey = Keys.hmacShaKeyFor(SECRET_STRING.getBytes(StandardCharsets.UTF_8));

    // Время жизни токенов (в миллисекундах)
    private static final long ACCESS_EXPIRATION_MS = 15 * 60 * 1000; // 15 минут
    private static final long REFRESH_EXPIRATION_MS = 7 * 24 * 60 * 60 * 1000; // 7 дней

    /**
     * Генерация Access-токена
     */
    public String generateAccessToken(String username, String jti) {
        return generateToken(username, "access", jti, ACCESS_EXPIRATION_MS);
    }

    /**
     * Генерация Refresh-токена
     */
    public String generateRefreshToken(String username, String jti) {
        return generateToken(username, "refresh", jti, REFRESH_EXPIRATION_MS);
    }

    private String generateToken(String username, String type, String jti, long expirationMs) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationMs);

        return Jwts.builder()
                .subject(username)
                .id(jti) // Уникальный идентификатор токена (jti)
                .claim("type", type) // Добавляем тип токена (access или refresh)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(secretKey)
                .compact();
    }

    /**
     * Проверка и извлечение данных из токена
     */
    public Claims validateAndExtractClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Проверка, что токен является Access-токеном
     */
    public boolean isAccessToken(String token) {
        Claims claims = validateAndExtractClaims(token);
        return "access".equals(claims.get("type", String.class));
    }

    /**
     * Проверка, что токен является Refresh-токеном
     */
    public boolean isRefreshToken(String token) {
        Claims claims = validateAndExtractClaims(token);
        return "refresh".equals(claims.get("type", String.class));
    }

    /**
     * Извлечение имени пользователя из токена
     */
    public String getUsernameFromToken(String token) {
        return validateAndExtractClaims(token).getSubject();
    }

    /**
     * Извлечение даты истечения токена
     */
    public Date getExpirationFromToken(String token) {
        return validateAndExtractClaims(token).getExpiration();
    }
}