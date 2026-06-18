package com.chatore.auth.service.Impl;


import com.chatore.auth.dto.request.*;
import com.chatore.auth.dto.response.AuthResponse;
import com.chatore.auth.dto.response.RefreshTokenResponse;
import com.chatore.auth.dto.response.UserProfileResponse;
import com.chatore.auth.entity.RefreshToken;
import com.chatore.auth.entity.User;
import com.chatore.auth.entity.enums.AccountStatus;
import com.chatore.auth.entity.enums.UserRole;
import com.chatore.auth.exception.custom.BadRequestException;
import com.chatore.auth.repository.UserRepository;
import com.chatore.auth.security.JwtService;
import com.chatore.auth.service.AuthService;
import com.chatore.auth.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenService refreshTokenService;

    private final JwtService jwtService;

    @Override
    public AuthResponse signUp(SignupRequest request) {


        if(userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("User with email already exists");
        }

        if(userRepository.existsByPhoneNumber(String.valueOf(request.getPhoneNumber()))) {
            throw new BadRequestException("User phone number already exists");
        }

        try {
            User user = User.builder()
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .email(request.getEmail())
                    .phoneNumber(request.getPhoneNumber())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .userRole(UserRole.CUSTOMER)
                    .accountStatus(AccountStatus.ACTIVE)
                    .build();

            userRepository.save(user);
            RefreshToken refreshToken =
                    refreshTokenService.createRefreshToken(user);


            String token = jwtService.generateToken(user.getEmail());

            return AuthResponse.builder()
                    .accessToken(token)
                    .tokenType("Bearer")
                    .refreshToken(refreshToken.getToken()).
                    build();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }

    @Override
    public AuthResponse login(LoginRequest loginRequest) {
        User user = userRepository
                .findByEmail(loginRequest.getEmail())
                .orElseThrow(()-> new BadRequestException("Invalid Credentials"));


        boolean passwordMatches = passwordEncoder.matches(
                loginRequest.getPassword(),
                user.getPassword()
        );

        if(!passwordMatches){
            throw new BadRequestException("Invalid Credentials");
        }

        String token = jwtService.generateToken(user.getEmail());
        RefreshToken refreshToken =
                refreshTokenService.createRefreshToken(user);

        return AuthResponse.builder()
                .accessToken(token)
                .tokenType("Bearer")
                .refreshToken(refreshToken.getToken()).
                build();

    }

    @Override
    public UserProfileResponse getUserProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user =  userRepository.findByEmail(email).orElseThrow(()-> new BadRequestException("Invalid Credentials"));

        return UserProfileResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .role(user.getUserRole().name())
                .emailVerified(user.isEmailVerified())
                .phoneVerified(user.isPhoneVerified())
                .build();
    }

    @Override
    public void changePassword(ChangePasswordRequest currentPasswordRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();
        User user =  userRepository.findByEmail(email).orElseThrow(()->
                new BadRequestException("Invalid Credentials"));

        boolean passwordMatches = passwordEncoder.matches(
                currentPasswordRequest.getCurrentPassword(),
                user.getPassword()
        );

        if(!passwordMatches){
            throw new BadRequestException("Wrong Current Password");
        }

        String encodedPassword = passwordEncoder.encode(currentPasswordRequest.getNewPassword());
        user.setPassword(encodedPassword);

        User savedUser = userRepository.save(user);

    }

    @Override
    public RefreshTokenResponse refreshToken(RefreshTokenRequest request) {
        RefreshToken refreshToken =
                refreshTokenService
                        .validateRefreshToken(
                                request.getRefreshToken()
                        );

        String accessToken =
                jwtService.generateToken(
                        refreshToken.getUser().getEmail()
                );

        return RefreshTokenResponse.builder()
                .accessToken(accessToken)
                .build();
    }

    @Override
    public void logout(LogoutRequest request) {
        refreshTokenService.revokeRefreshToken(
                request.getRefreshToken()
        );
    }


}
