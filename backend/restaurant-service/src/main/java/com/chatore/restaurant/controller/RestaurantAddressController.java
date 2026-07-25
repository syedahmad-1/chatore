package com.chatore.restaurant.controller;

import com.chatore.restaurant.dto.request.CreateRestaurantAddressRequest;
import com.chatore.restaurant.dto.request.UpdateRestaurantAddressRequest;
import com.chatore.restaurant.dto.response.RestaurantAddressResponse;
import com.chatore.restaurant.service.RestaurantAddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/restaurants/{restaurantId}/address")
public class RestaurantAddressController {

    private final RestaurantAddressService restaurantAddressService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RestaurantAddressResponse createAddress(
            @PathVariable UUID restaurantId,
            @Valid @RequestBody CreateRestaurantAddressRequest request) {

        return restaurantAddressService.createAddress(
                restaurantId,
                request
        );
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public RestaurantAddressResponse getAddress(
            @PathVariable UUID restaurantId) {

        return restaurantAddressService.getAddress(restaurantId);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public RestaurantAddressResponse updateAddress(
            @PathVariable UUID restaurantId,
            @Valid @RequestBody UpdateRestaurantAddressRequest request) {

        return restaurantAddressService.updateAddress(
                restaurantId,
                request
        );
    }
    

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAddress(
            @PathVariable UUID restaurantId) {

        restaurantAddressService.deleteAddress(restaurantId);
    }
}
