package com.chatore.restaurant.security;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class MockCurrentUserProvider implements CurrentUserProvider {

    private static final UUID TEST_USER_ID =
            UUID.fromString("11111111-1111-1111-1111-111111111111");

    @Override
    public UUID getCurrentUserId() {
        return TEST_USER_ID;
    }
}