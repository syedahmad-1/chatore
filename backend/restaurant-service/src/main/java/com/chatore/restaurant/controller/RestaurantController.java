package com.chatore.restaurant.controller;

import com.chatore.restaurant.dto.request.CreateRestaurantRequest;
import com.chatore.restaurant.dto.request.UpdateRestaurantRequest;
import com.chatore.restaurant.dto.response.RestaurantResponse;
import com.chatore.restaurant.service.RestaurantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/restaurants")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RestaurantResponse createRestaurant(
            @Valid @RequestBody CreateRestaurantRequest request) {

        return restaurantService.createRestaurant(request);
    }

    @GetMapping("/{restaurantId}")
    public RestaurantResponse getRestaurantById(
            @PathVariable UUID restaurantId) {

        return restaurantService.getRestaurantById(restaurantId);
    }

    @GetMapping
    public List<RestaurantResponse> getAllRestaurants() {

        return restaurantService.getAllRestaurants();
    }

    @PutMapping("/{restaurantId}")
    public RestaurantResponse updateRestaurant(
            @PathVariable UUID restaurantId,
            @Valid @RequestBody UpdateRestaurantRequest request) {

        return restaurantService.updateRestaurant(restaurantId, request);
    }

    @DeleteMapping("/{restaurantId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRestaurant(
            @PathVariable UUID restaurantId) {

        restaurantService.deleteRestaurant(restaurantId);
    }
}