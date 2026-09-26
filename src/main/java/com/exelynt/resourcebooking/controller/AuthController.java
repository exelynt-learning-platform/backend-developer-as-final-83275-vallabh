package com.exelynt.resourcebooking.controller;

import com.exelynt.resourcebooking.dto.auth.LoginRequest;
import com.exelynt.resourcebooking.dto.auth.LoginResponse;
import com.exelynt.resourcebooking.dto.auth.SignupRequest;
import com.exelynt.resourcebooking.dto.auth.SignupResponse;
import com.exelynt.resourcebooking.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<SignupResponse> signup(
            @Valid @RequestBody SignupRequest request
    ) {
        SignupResponse response = authService.signup(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request
    ) {
        LoginResponse response = authService.login(request);

        return ResponseEntity.ok(response);
    }
}