package com.chatore.auth.service;

import com.chatore.auth.dto.request.LoginRequest;
import com.chatore.auth.dto.request.SignupRequest;

public interface AuthService {

    void signUp(SignupRequest signupRequest);

}
