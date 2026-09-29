package com.exelynt.resourcebooking.service;

import com.exelynt.resourcebooking.dto.auth.LoginRequest;
import com.exelynt.resourcebooking.dto.auth.LoginResponse;
import com.exelynt.resourcebooking.dto.auth.SignupRequest;
import com.exelynt.resourcebooking.dto.auth.SignupResponse;
import com.exelynt.resourcebooking.entity.User;
import com.exelynt.resourcebooking.enums.Role;
import com.exelynt.resourcebooking.exception.DuplicateEmailException;
import com.exelynt.resourcebooking.exception.InvalidCredentialsException;
import com.exelynt.resourcebooking.repository.UserRepository;
import com.exelynt.resourcebooking.security.JwtService;
import com.exelynt.resourcebooking.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    private AuthServiceImpl authService;

    @BeforeEach
    void setUp() {
        authService = new AuthServiceImpl(
                userRepository,
                passwordEncoder,
                jwtService
        );
    }

    @Test
    void shouldSignupUserSuccessfully() {

        SignupRequest request =
                new SignupRequest(
                        "user@example.com",
                        "User@12345"
                );

        when(userRepository.existsByEmail(request.email()))
                .thenReturn(false);

        when(passwordEncoder.encode(request.password()))
                .thenReturn("encoded-password");

        User savedUser = mock(User.class);

        when(savedUser.getId()).thenReturn(1L);
        when(savedUser.getEmail()).thenReturn(request.email());

        when(userRepository.save(any(User.class)))
                .thenReturn(savedUser);

        SignupResponse response =
                authService.signup(request);

        assertNotNull(response);
        assertEquals(1L, response.id());
        assertEquals("user@example.com", response.email());

        verify(userRepository).existsByEmail(request.email());
        verify(passwordEncoder).encode(request.password());
        verify(userRepository).save(any(User.class));
    }
    @Test
    void shouldRejectDuplicateEmail() {

        SignupRequest request =
                new SignupRequest(
                        "user@example.com",
                        "User@12345"
                );

        when(userRepository.existsByEmail(request.email()))
                .thenReturn(true);

        assertThrows(
                DuplicateEmailException.class,
                () -> authService.signup(request)
        );

        verify(userRepository).existsByEmail(request.email());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void shouldLoginSuccessfully() {

        LoginRequest request =
                new LoginRequest(
                        "user@example.com",
                        "User@12345"
                );

        User user =
                new User(
                        "user@example.com",
                        "encoded-password",
                        Role.USER
                );

        when(userRepository.findByEmail(request.email()))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches(
                request.password(),
                user.getPassword()
        )).thenReturn(true);

        when(jwtService.generateToken(user.getEmail()))
                .thenReturn("jwt-token");

        LoginResponse response =
                authService.login(request);

        assertNotNull(response);
        assertEquals("jwt-token", response.token());

        verify(userRepository).findByEmail(request.email());
        verify(passwordEncoder).matches(
                request.password(),
                user.getPassword()
        );
        verify(jwtService).generateToken(user.getEmail());
    }

    @Test
    void shouldRejectInvalidPassword() {

        LoginRequest request =
                new LoginRequest(
                        "user@example.com",
                        "WrongPassword"
                );

        User user =
                new User(
                        "user@example.com",
                        "encoded-password",
                        Role.USER
                );

        when(userRepository.findByEmail(request.email()))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches(
                request.password(),
                user.getPassword()
        )).thenReturn(false);

        assertThrows(
                InvalidCredentialsException.class,
                () -> authService.login(request)
        );

        verify(jwtService, never()).generateToken(anyString());
    }

    @Test
    void shouldRejectUnknownEmail() {

        LoginRequest request =
                new LoginRequest(
                        "unknown@example.com",
                        "User@12345"
                );

        when(userRepository.findByEmail(request.email()))
                .thenReturn(Optional.empty());

        assertThrows(
                InvalidCredentialsException.class,
                () -> authService.login(request)
        );

        verify(jwtService, never()).generateToken(anyString());
    }
}