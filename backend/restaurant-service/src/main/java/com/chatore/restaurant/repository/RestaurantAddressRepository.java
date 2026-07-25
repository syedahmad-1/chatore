package com.chatore.restaurant.repository;

import com.chatore.restaurant.entity.RestaurantAddress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RestaurantAddressRepository extends JpaRepository<RestaurantAddress, UUID> {

        Optional<RestaurantAddress> findByRestaurantId(UUID restaurantId);

        boolean existsByRestaurantId(UUID restaurantId);

        void deleteByRestaurantId(UUID restaurantId);
}
