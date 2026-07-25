package com.chatore.restaurant.exception;

public class RestaurantAddressNotFoundException extends RuntimeException {
    public RestaurantAddressNotFoundException(String message) {
        super(message);
    }
}
