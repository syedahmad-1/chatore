package com.chatore.restaurant.service;

import com.chatore.restaurant.dto.request.CreateRestaurantRequest;
import com.chatore.restaurant.dto.request.UpdateRestaurantRequest;
import com.chatore.restaurant.dto.response.RestaurantResponse;

import java.util.List;
import java.util.UUID;

public interface RestaurantService {

    RestaurantResponse createRestaurant(CreateRestaurantRequest request);

    RestaurantResponse getRestaurantById(UUID restaurantId);

    List<RestaurantResponse> getAllRestaurants();

    RestaurantResponse updateRestaurant(
            UUID restaurantId,
            UpdateRestaurantRequest request);

    void deleteRestaurant(UUID restaurantId);
}