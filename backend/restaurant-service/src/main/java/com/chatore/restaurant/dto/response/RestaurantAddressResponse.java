package com.chatore.restaurant.dto.response;

import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RestaurantAddressResponse {

    private UUID id;

    private UUID restaurantId;

    private String street;

    private String landmark;

    private String area;

    private String city;

    private String state;

    private String country;

    private String postalCode;

    private Double latitude;

    private Double longitude;

    private Instant createdAt;

    private Instant updatedAt;
}