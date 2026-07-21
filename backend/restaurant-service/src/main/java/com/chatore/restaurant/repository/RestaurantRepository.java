package com.chatore.restaurant.repository;

import com.chatore.restaurant.entity.Restaurant;
import com.chatore.restaurant.enums.AvailabilityStatus;
import com.chatore.restaurant.enums.RestaurantStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RestaurantRepository extends JpaRepository<Restaurant, UUID> {

    Optional<Restaurant> findByOwnerId(UUID ownerId);

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);

    List<Restaurant> findByStatus(RestaurantStatus status);

    List<Restaurant> findByAvailabilityStatus(AvailabilityStatus availabilityStatus);

    List<Restaurant> findByNameContainingIgnoreCase(String keyword);

    List<Restaurant> findAllByOrderByCreatedAtDesc();
}
