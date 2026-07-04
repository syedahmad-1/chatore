package com.chatore.auth.service;

import com.chatore.auth.entity.RefreshToken;
import com.chatore.auth.entity.User;

public interface RefreshTokenService {

    RefreshToken createRefreshToken(User user);

    RefreshToken validateRefreshToken(String token);

    void revokeRefreshToken(String token);



}