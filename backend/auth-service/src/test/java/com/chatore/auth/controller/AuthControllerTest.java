package com.chatore.auth.controller;

import com.chatore.auth.dto.request.ChangePasswordRequest;
import com.chatore.auth.dto.request.LoginRequest;
import com.chatore.auth.dto.request.LogoutRequest;
import com.chatore.auth.dto.request.RefreshTokenRequest;
import com.chatore.auth.dto.request.SignupRequest;
import com.chatore.auth.dto.response.AuthResponse;
import com.chatore.auth.dto.response.RefreshTokenResponse;
import com.chatore.auth.dto.response.UserProfileResponse;
import com.chatore.auth.exception.custom.BadRequestException;
import com.chatore.auth.service.AuthService;
import com.chatore.auth.security.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @MockBean
    private JwtService jwtService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void signUp_success() throws Exception {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("test@example.com");
        signupRequest.setPassword("password");
        signupRequest.setFirstName("John");
        signupRequest.setLastName("Doe");
        AuthResponse authResponse = AuthResponse.builder()
                .accessToken("jwt_token")
                .refreshToken("refresh_token")
                .tokenType("Bearer")
                .build();

        when(authService.signUp(any(SignupRequest.class))).thenReturn(authResponse);

        mockMvc.perform(post("/api/v1/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("User Registered Successfully"));
    }

    @Test
    void login_success() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("password");
        AuthResponse authResponse = AuthResponse.builder()
                .accessToken("jwt_token")
                .refreshToken("refresh_token")
                .tokenType("Bearer")
                .build();

        when(authService.login(any(LoginRequest.class))).thenReturn(authResponse);

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("User Logged in Successfully"));
    }

    @Test
    void me_success() throws Exception {
        UserProfileResponse userProfileResponse = UserProfileResponse.builder()
                .firstName("John")
                .email("test@example.com")
                .build();

        when(authService.getUserProfile()).thenReturn(userProfileResponse);

        mockMvc.perform(get("/api/v1/auth/me")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Current User Profile fetched successfully"))
                .andExpect(jsonPath("$.data.email").value("test@example.com"));
    }

    @Test
    void changePassword_success() throws Exception {
        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest();
        changePasswordRequest.setCurrentPassword("oldPassword");
        changePasswordRequest.setNewPassword("newPassword");

        doNothing().when(authService).changePassword(any(ChangePasswordRequest.class));

        mockMvc.perform(post("/api/v1/auth/change-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(changePasswordRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Password changed successfully"));
    }

    @Test
    void changePassword_badRequest() throws Exception {
        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest();
        changePasswordRequest.setCurrentPassword("wrongOldPassword");
        changePasswordRequest.setNewPassword("newPassword");

        doThrow(new BadRequestException("Invalid old password")).when(authService).changePassword(any(ChangePasswordRequest.class));

        mockMvc.perform(post("/api/v1/auth/change-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(changePasswordRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Invalid old password"));
    }

    @Test
    void refreshToken_success() throws Exception {
        RefreshTokenRequest refreshTokenRequest = new RefreshTokenRequest();
        refreshTokenRequest.setRefreshToken("old_refresh_token");
        RefreshTokenResponse refreshTokenResponse = RefreshTokenResponse.builder()
                .accessToken("new_jwt_token")
                .build();

        when(authService.refreshToken(any(RefreshTokenRequest.class))).thenReturn(refreshTokenResponse);

        mockMvc.perform(post("/api/v1/auth/refresh-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(refreshTokenRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Token refreshed"))
                .andExpect(jsonPath("$.data.accessToken").value("new_jwt_token"));
    }

    @Test
    void logout_success() throws Exception {
        LogoutRequest logoutRequest = new LogoutRequest();
        logoutRequest.setRefreshToken("some_refresh_token");

        doNothing().when(authService).logout(any(LogoutRequest.class));

        mockMvc.perform(post("/api/v1/auth/logout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(logoutRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Logged out successfully"));
    }
}
