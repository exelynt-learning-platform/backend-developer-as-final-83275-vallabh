package com.exelynt.resourcebooking.service;

import com.exelynt.resourcebooking.dto.auth.LoginRequest;
import com.exelynt.resourcebooking.dto.auth.LoginResponse;
import com.exelynt.resourcebooking.dto.auth.SignupRequest;
import com.exelynt.resourcebooking.dto.auth.SignupResponse;

public interface AuthService {

    SignupResponse signup(SignupRequest request);

    LoginResponse login(LoginRequest request);
}