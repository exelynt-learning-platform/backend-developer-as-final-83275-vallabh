package com.exelynt.resourcebooking.security;

import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import io.jsonwebtoken.ExpiredJwtException;

class JwtServiceTest {

    private static final String SECRET =
            "ResourceBookingSystemDevelopmentSecret2026SecureKey";

    @Test
    void shouldGenerateTokenSuccessfully() {

        JwtService jwtService =
                new JwtService(SECRET, 3600000);

        String token = jwtService.generateToken("user@example.com");

        assertNotNull(token);
        assertFalse(token.isBlank());
    }

    @Test
    void shouldExtractUsernameFromToken() {

        JwtService jwtService =
                new JwtService(SECRET, 3600000);

        String token =
                jwtService.generateToken("user@example.com");

        String username =
                jwtService.extractUsername(token);

        assertEquals("user@example.com", username);
    }

    @Test
    void shouldValidateTokenForCorrectUsername() {

        JwtService jwtService =
                new JwtService(SECRET, 3600000);

        String token =
                jwtService.generateToken("user@example.com");

        boolean valid =
                jwtService.isTokenValid(
                        token,
                        "user@example.com"
                );

        assertTrue(valid);
    }

    @Test
    void shouldRejectTokenForDifferentUsername() {

        JwtService jwtService =
                new JwtService(SECRET, 3600000);

        String token =
                jwtService.generateToken("user@example.com");

        boolean valid =
                jwtService.isTokenValid(
                        token,
                        "another@example.com"
                );

        assertFalse(valid);
    }

    @Test
    void shouldRejectExpiredToken() {

        JwtService jwtService =
                new JwtService(SECRET, -1000);

        String token =
                jwtService.generateToken("user@example.com");

        assertThrows(
                ExpiredJwtException.class,
                () -> jwtService.isTokenValid(
                        token,
                        "user@example.com"
                )
        );
    }

    @Test
    void shouldRejectInvalidToken() {

        JwtService jwtService =
                new JwtService(SECRET, 3600000);

        String invalidToken = "invalid.jwt.token";

        assertThrows(
                JwtException.class,
                () -> jwtService.extractUsername(invalidToken)
        );
    }
}