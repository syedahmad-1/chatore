package com.chatore.restaurant.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateRestaurantRequest {

    private String name;

    private String description;

    private String phone;

    @Email(message = "Invalid email address")
    private String email;

    private String logoUrl;

    private String bannerUrl;

    private LocalTime openingTime;

    private LocalTime closingTime;

    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal deliveryRadiusKm;

    @DecimalMin(value = "0.0", inclusive = true)
    private BigDecimal minimumOrderAmount;
}