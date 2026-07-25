package com.chatore.restaurant.service.impl;

import com.chatore.restaurant.dto.request.CreateRestaurantAddressRequest;
import com.chatore.restaurant.dto.request.UpdateRestaurantAddressRequest;
import com.chatore.restaurant.dto.response.RestaurantAddressResponse;
import com.chatore.restaurant.entity.Restaurant;
import com.chatore.restaurant.entity.RestaurantAddress;
import com.chatore.restaurant.exception.DuplicateResourceException;
import com.chatore.restaurant.exception.RestaurantAddressNotFoundException;
import com.chatore.restaurant.exception.RestaurantNotFoundException;
import com.chatore.restaurant.exception.UnauthorizedOperationException;
import com.chatore.restaurant.mapper.RestaurantAddressMapper;
import com.chatore.restaurant.repository.RestaurantAddressRepository;
import com.chatore.restaurant.repository.RestaurantRepository;
import com.chatore.restaurant.security.CurrentUserProvider;
import com.chatore.restaurant.service.RestaurantAddressService;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
@Transactional
@RequiredArgsConstructor
public class RestaurantAddressServiceImpl implements RestaurantAddressService {

    private final RestaurantAddressRepository restaurantAddressRepository;
    private final RestaurantRepository restaurantRepository;
    private final CurrentUserProvider currentUserProvider;
    private final RestaurantAddressMapper restaurantAddressMapper;


    @Override
    public RestaurantAddressResponse createAddress(UUID restaurantId, CreateRestaurantAddressRequest request) {

        Restaurant restaurant = findRestaurantById(restaurantId);
        validateOwnership(restaurant);


        if (restaurantAddressRepository.existsByRestaurantId(restaurantId)) {
            throw new DuplicateResourceException(
                    "Restaurant already has an address."
            );
        }

        RestaurantAddress address = restaurantAddressMapper.toEntity(request);
        address.setRestaurant(restaurant);

        RestaurantAddress savedAddress =
                restaurantAddressRepository.save(address);

        return restaurantAddressMapper.toResponse(savedAddress);
    }

    @Override
    @Transactional(readOnly = true)
    public RestaurantAddressResponse getAddress(UUID restaurantId) {

        RestaurantAddress address = findAddressByRestaurantId(restaurantId);

        return restaurantAddressMapper.toResponse(address);
    }

    @Override
    public RestaurantAddressResponse updateAddress(
            UUID restaurantId,
            UpdateRestaurantAddressRequest request) {

        Restaurant restaurant = findRestaurantById(restaurantId);

        validateOwnership(restaurant);

        RestaurantAddress address = findAddressByRestaurantId(restaurantId);

        restaurantAddressMapper.updateAddress(request, address);

        RestaurantAddress updatedAddress =
                restaurantAddressRepository.save(address);

        return restaurantAddressMapper.toResponse(updatedAddress);
    }

    @Override
    public void deleteAddress(UUID restaurantId) {

        Restaurant restaurant = findRestaurantById(restaurantId);

        validateOwnership(restaurant);

        RestaurantAddress address = findAddressByRestaurantId(restaurantId);

        restaurantAddressRepository.delete(address);
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

    private RestaurantAddress findAddressByRestaurantId(UUID restaurantId) {

        return restaurantAddressRepository
                .findByRestaurantId(restaurantId)
                .orElseThrow(() ->
                        new RestaurantAddressNotFoundException(
                                "Address not found for restaurant id: "
                                        + restaurantId
                        ));
    }
}
