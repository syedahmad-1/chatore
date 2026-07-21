package com.chatore.restaurant.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateRestaurantRequest {

    @NotBlank(message = "Restaurant name is required")
    private String name;

    private String description;

    @NotBlank(message = "Phone number is required")
    private String phone;

    @Email(message = "Invalid email address")
    @NotBlank(message = "Email is required")
    private String email;

    private String logoUrl;

    private String bannerUrl;

    @NotNull(message = "Opening time is required")
    private LocalTime openingTime;

    @NotNull(message = "Closing time is required")
    private LocalTime closingTime;

    @NotNull(message = "Delivery radius is required")
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal deliveryRadiusKm;

    @NotNull(message = "Minimum order amount is required")
    @DecimalMin(value = "0.0", inclusive = true)
    private BigDecimal minimumOrderAmount;
}