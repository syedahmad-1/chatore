package com.chatore.restaurant.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "restaurant_address"
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RestaurantAddress extends BaseEntity{


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false, unique = true)
    private Restaurant restaurant;

    @Column(nullable = false)
    private String street;

    private String landmark;

    @Column(nullable = false)
    private String area;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String state;

    @Column(nullable = false)
    private String country;

    @Column(nullable = false)
    private String zipcode;

    private Double latitude;

    private Double longitude;
}
