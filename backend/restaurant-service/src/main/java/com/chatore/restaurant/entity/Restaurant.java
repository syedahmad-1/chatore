package com.chatore.restaurant.entity;


import com.chatore.restaurant.enums.AvailabilityStatus;
import com.chatore.restaurant.enums.RestaurantStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalTime;
import java.util.UUID;


@Entity
@Table(
        name = "restaurants",
        indexes = {
                @Index(name = "idx_restaurant_owner", columnList = "owner_id"),
                @Index(name = "idx_restaurant_status", columnList = "status"),
                @Index(name = "idx_restaurant_name", columnList = "name")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Restaurant extends BaseEntity {


    @Column(name = "owner_id", nullable = false)
    private UUID ownerId;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false, length = 15)
    private String phone;

    @Column(nullable = false, length = 150)
    private String email;

    private String logoUrl;

    private String bannerUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RestaurantStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AvailabilityStatus availabilityStatus;

    @Column(nullable = false)
    private LocalTime openingTime;

    @Column(nullable = false)
    private LocalTime closingTime;

    @Column(nullable = false)
    private BigDecimal deliveryRadiusKm;

    @Column(nullable = false)
    private BigDecimal minimumOrderAmount;

    @Column(nullable = false)
    private Double averageRating;

    @Column(nullable = false)
    private Integer totalReviews;
}