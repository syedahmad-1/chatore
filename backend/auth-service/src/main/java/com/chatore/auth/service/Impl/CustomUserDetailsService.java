package com.chatore.auth.service.Impl;

import com.chatore.auth.entity.User;
import com.chatore.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        UUID userUuid;
        try {
            userUuid = UUID.fromString(userId);
        } catch (IllegalArgumentException ex) {
            throw new UsernameNotFoundException("Invalid user id: " + userId, ex);
        }

        return userRepository.findById(userUuid)
                .map(user -> new org.springframework.security.core.userdetails.User(
                        user.getId().toString(),
                        user.getPassword(),
                        List.of(
                                new SimpleGrantedAuthority(
                                        "ROLE_" + user.getUserRole().name()
                                )
                        )
                ))
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + userId));
    }
}
