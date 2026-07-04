package com.chatore.auth.service.Impl;


import com.chatore.auth.entity.RefreshToken;
import com.chatore.auth.entity.User;
import com.chatore.auth.repository.RefreshTokenRepository;
import com.chatore.auth.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    @Override
    public RefreshToken createRefreshToken(User user) {

        RefreshToken refreshToken = RefreshToken.builder()
                .token(UUID.randomUUID().toString())
                .expiresAt(
                        Instant.now().plus(7,
                                ChronoUnit.DAYS)
                )
                .user(user)
                .build();

        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    public RefreshToken validateRefreshToken(String token) {

        RefreshToken refreshToken = refreshTokenRepository.findByToken(token).orElseThrow(()->
                new RuntimeException("Invalid refresh token"));

        if(refreshToken.getRevoked()==Boolean.TRUE){
            throw new RuntimeException(
                    "Refresh token revoked"
            );
        }

        if (refreshToken.getExpiresAt()
                .isBefore(Instant.now())) {

            throw new RuntimeException(
                    "Refresh token expired"
            );
        }

        return refreshToken;
    }

    @Override
    public void revokeRefreshToken(String token) {
        RefreshToken refreshToken =
                validateRefreshToken(token);

        refreshToken.setRevoked(true);

        refreshTokenRepository.save(refreshToken);

    }


}
