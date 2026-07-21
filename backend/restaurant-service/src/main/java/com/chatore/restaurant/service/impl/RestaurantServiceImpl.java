package com.chatore.restaurant.service.impl;

import com.chatore.restaurant.dto.request.CreateRestaurantRequest;
import com.chatore.restaurant.dto.request.UpdateRestaurantRequest;
import com.chatore.restaurant.dto.response.RestaurantResponse;
import com.chatore.restaurant.entity.Restaurant;
import com.chatore.restaurant.exception.RestaurantNotFoundException;
import com.chatore.restaurant.exception.UnauthorizedOperationException;
import com.chatore.restaurant.mapper.RestaurantMapper;
import com.chatore.restaurant.repository.RestaurantRepository;
import com.chatore.restaurant.security.CurrentUserProvider;
import com.chatore.restaurant.service.RestaurantService;
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
    private final CurrentUserProvider currentUserProvider;

    @Override
    public RestaurantResponse createRestaurant(CreateRestaurantRequest request) {

        Restaurant restaurant = restaurantMapper.toEntity(request);
        restaurant.setOwnerId(currentUserProvider.getCurrentUserId());

        Restaurant savedRestaurant = restaurantRepository.save(restaurant);

        return restaurantMapper.toResponse(savedRestaurant);
    }

    @Override
    @Transactional(readOnly = true)
    public RestaurantResponse getRestaurantById(UUID restaurantId) {

        Restaurant restaurant = findRestaurantById(restaurantId);

        return restaurantMapper.toResponse(restaurant);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RestaurantResponse> getAllRestaurants() {

        return restaurantRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(restaurantMapper::toResponse)
                .toList();
    }

    @Override
    public RestaurantResponse updateRestaurant(UUID restaurantId,
                                               UpdateRestaurantRequest request) {

        Restaurant restaurant = findRestaurantById(restaurantId);

        validateOwnership(restaurant);

        restaurantMapper.updateRestaurant(request, restaurant);

        Restaurant updatedRestaurant = restaurantRepository.save(restaurant);

        return restaurantMapper.toResponse(updatedRestaurant);
    }

    @Override
    public void deleteRestaurant(UUID restaurantId) {

        Restaurant restaurant = findRestaurantById(restaurantId);

        validateOwnership(restaurant);

        restaurantRepository.delete(restaurant);
    }

    /**
     * Finds a restaurant by its ID.
     */
    private Restaurant findRestaurantById(UUID restaurantId) {

        return restaurantRepository.findById(restaurantId)
                .orElseThrow(() ->
                        new RestaurantNotFoundException(
                                "Restaurant not found with id: " + restaurantId
                        ));
    }

    /**
     * Ensures the current authenticated user owns the restaurant.
     */
    private void validateOwnership(Restaurant restaurant) {

        UUID currentUserId = currentUserProvider.getCurrentUserId();

        if (!restaurant.getOwnerId().equals(currentUserId)) {
            throw new UnauthorizedOperationException(
                    "You are not authorized to modify this restaurant."
            );
        }
    }
}