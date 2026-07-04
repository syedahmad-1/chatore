package com.chatore.auth.repository;

import com.chatore.auth.entity.User;
import com.chatore.auth.entity.UserAddress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserAddressRepository extends JpaRepository<UserAddress, UUID> {

    List<UserAddress> findByUser(User user);

    Optional<UserAddress> findByIdAndUser(UUID id, User user);

    Optional<UserAddress> findByUserAndIsDefaultTrue(User user);

    Optional<UserAddress> findTopByUserOrderByUpdatedAtDesc(User user);

    long countByUser(User user);
}