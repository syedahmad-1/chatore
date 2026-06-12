package com.chatore.auth.controller;


import com.chatore.auth.dto.request.ChangePasswordRequest;
import com.chatore.auth.dto.request.LoginRequest;
import com.chatore.auth.dto.request.SignupRequest;
import com.chatore.auth.dto.response.ApiResponse;
import com.chatore.auth.dto.response.AuthResponse;
import com.chatore.auth.dto.response.UserProfileResponse;
import com.chatore.auth.exception.custom.BadRequestException;
import com.chatore.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<AuthResponse>> signUp(@Valid @RequestBody SignupRequest request){
        AuthResponse authResponse = authService.signUp(request);

        return ResponseEntity.ok(
                ApiResponse.<AuthResponse>builder()
                        .data(authResponse)
                        .success(true)
                        .message("User Registered Successfully")
                        .build()
        );
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request){
        AuthResponse authResponse = authService.login(request);

        return ResponseEntity.ok(
                ApiResponse.<AuthResponse>builder()
                        .data(authResponse)
                        .message("User Logged in Successfully")
                        .success(true)
                        .build()
        );
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserProfileResponse>> me() {

        UserProfileResponse userProfileResponse = authService.getUserProfile();
        return ResponseEntity.ok(
                ApiResponse.<UserProfileResponse>builder()
                        .data(userProfileResponse)
                        .message("Current User Profile fetched successfully")
                        .success(true)
                        .build()
        );
    }

    @PostMapping("/change-password")
    public ResponseEntity<ApiResponse<Void>> changePassword(@Valid @RequestBody ChangePasswordRequest request){
        try {
            authService.changePassword(request);
            return ResponseEntity.ok(
                    ApiResponse.<Void>builder()
                            .message("Password changed successfully")
                            .success(true)
                            .build()
            );
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.<Void>builder()
                            .message(e.getMessage())
                            .success(false)
                            .build()
            );
        }
    }

}
