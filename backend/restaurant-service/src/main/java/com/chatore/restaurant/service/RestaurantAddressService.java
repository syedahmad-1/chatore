package com.chatore.restaurant.service;

import com.chatore.restaurant.dto.request.CreateRestaurantAddressRequest;
import com.chatore.restaurant.dto.request.UpdateRestaurantAddressRequest;
import com.chatore.restaurant.dto.response.RestaurantAddressResponse;

import java.util.UUID;

public interface RestaurantAddressService {

    RestaurantAddressResponse createAddress(
            UUID restaurantId,
            CreateRestaurantAddressRequest request);

    RestaurantAddressResponse getAddress(UUID restaurantId);

    RestaurantAddressResponse updateAddress(
            UUID restaurantId,
            UpdateRestaurantAddressRequest request);

    void deleteAddress(UUID restaurantId);
}
