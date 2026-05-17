package com.chatore.auth.service.Impl;

import com.chatore.auth.config.SecurityBeansConfig;
import com.chatore.auth.dto.request.LoginRequest;
import com.chatore.auth.dto.request.SignupRequest;
import com.chatore.auth.entity.User;
import com.chatore.auth.entity.enums.AccountStatus;
import com.chatore.auth.entity.enums.UserRole;
import com.chatore.auth.exception.custom.BadRequestException;
import com.chatore.auth.repository.UserRepository;
import com.chatore.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void signUp(SignupRequest request) {

        if(userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("User with email already exists");
        }

        if(userRepository.existsByPhoneNumber(String.valueOf(request.getPhoneNumber()))) {
            throw new BadRequestException("User phone number already exists");
        }

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


    }

}
