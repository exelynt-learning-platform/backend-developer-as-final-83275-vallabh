package com.exelynt.resourcebooking.service.impl;

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
import com.exelynt.resourcebooking.service.AuthService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthServiceImpl(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Override
    public SignupResponse signup(SignupRequest request) {

        if (userRepository.existsByEmail(request.email())) {
            throw new DuplicateEmailException(
                    "Email is already registered"
            );
        }

        String encodedPassword = passwordEncoder.encode(request.password());

        User user = new User(
                request.email(),
                encodedPassword,
                Role.USER
        );

        User savedUser = userRepository.save(user);

        return new SignupResponse(
                savedUser.getId(),
                savedUser.getEmail()
        );
    }

    @Override
    public LoginResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() ->
                        new InvalidCredentialsException(
                                "Invalid email or password"
                        )
                );

        if (!user.isEnabled()) {
            throw new InvalidCredentialsException(
                    "Invalid email or password"
            );
        }

        if (!passwordEncoder.matches(
                request.password(),
                user.getPassword()
        )) {
            throw new InvalidCredentialsException(
                    "Invalid email or password"
            );
        }

        String token = jwtService.generateToken(user.getEmail());

        return new LoginResponse(token);
    }
}