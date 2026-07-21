package com.chatore.restaurant.dto.response;

import com.chatore.restaurant.enums.AvailabilityStatus;
import com.chatore.restaurant.enums.RestaurantStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RestaurantResponse {

    private UUID id;

    private UUID ownerId;

    private String name;

    private String description;

    private String phone;

    private String email;

    private String logoUrl;

    private String bannerUrl;

    private RestaurantStatus status;

    private AvailabilityStatus availabilityStatus;

    private LocalTime openingTime;

    private LocalTime closingTime;

    private BigDecimal deliveryRadiusKm;

    private BigDecimal minimumOrderAmount;

    private Double averageRating;

    private Integer totalReviews;

    private Instant createdAt;

    private Instant updatedAt;
}