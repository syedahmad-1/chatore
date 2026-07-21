package com.chatore.restaurant.service.impl;

import com.chatore.restaurant.dto.request.CreateRestaurantRequest;
import com.chatore.restaurant.dto.request.UpdateRestaurantRequest;
import com.chatore.restaurant.dto.response.RestaurantResponse;
import com.chatore.restaurant.entity.Restaurant;
import com.chatore.restaurant.mapper.RestaurantMapper;
import com.chatore.restaurant.repository.RestaurantRepository;
import com.chatore.restaurant.service.RestaurantService;
import com.chatore.restaurant.exception.RestaurantNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final RestaurantMapper restaurantMapper;

    @Override
    public RestaurantResponse createRestaurant(CreateRestaurantRequest request) {

        Restaurant restaurant = restaurantMapper.toEntity(request);

        // TODO: Replace with authenticated user's ID from JWT
        restaurant.setOwnerId(UUID.randomUUID());

        Restaurant savedRestaurant = restaurantRepository.save(restaurant);

        return restaurantMapper.toResponse(savedRestaurant);
    }

    @Override
    @Transactional(readOnly = true)
    public RestaurantResponse getRestaurantById(UUID restaurantId) {

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() ->
                        new RestaurantNotFoundException(
                                "Restaurant not found with id: " + restaurantId));

        return restaurantMapper.toResponse(restaurant);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RestaurantResponse> getAllRestaurants() {

        return restaurantRepository.findAll()
                .stream()
                .map(restaurantMapper::toResponse)
                .toList();
    }

    @Override
    public RestaurantResponse updateRestaurant(UUID restaurantId,
                                               UpdateRestaurantRequest request) {

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() ->
                        new RestaurantNotFoundException(
                                "Restaurant not found with id: " + restaurantId));

        restaurantMapper.updateRestaurant(request, restaurant);

        Restaurant updatedRestaurant = restaurantRepository.save(restaurant);

        return restaurantMapper.toResponse(updatedRestaurant);
    }

    @Override
    public void deleteRestaurant(UUID restaurantId) {

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() ->
                        new RestaurantNotFoundException(
                                "Restaurant not found with id: " + restaurantId));

        restaurantRepository.delete(restaurant);
    }
}