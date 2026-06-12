package com.chatore.auth.service;

import com.chatore.auth.dto.request.ChangePasswordRequest;
import com.chatore.auth.dto.request.LoginRequest;
import com.chatore.auth.dto.request.SignupRequest;
import com.chatore.auth.dto.response.AuthResponse;
import com.chatore.auth.dto.response.UserProfileResponse;

public interface AuthService {

    AuthResponse signUp(SignupRequest signupRequest);

    AuthResponse login(LoginRequest loginRequest);

    UserProfileResponse getUserProfile();

    void changePassword(ChangePasswordRequest changePasswordRequest);



}
