package com.chatore.auth.service;

import com.chatore.auth.dto.request.*;
import com.chatore.auth.dto.response.AuthResponse;
import com.chatore.auth.dto.response.RefreshTokenResponse;
import com.chatore.auth.dto.response.UserProfileResponse;

public interface AuthService {

    AuthResponse signUp(SignupRequest signupRequest);

    AuthResponse login(LoginRequest loginRequest);

    UserProfileResponse getUserProfile();

    void changePassword(ChangePasswordRequest changePasswordRequest);


    RefreshTokenResponse refreshToken(RefreshTokenRequest request);

    void logout(LogoutRequest request);
}
