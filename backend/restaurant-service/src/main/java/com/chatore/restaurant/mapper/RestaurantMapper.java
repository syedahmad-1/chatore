package com.chatore.restaurant.mapper;

import com.chatore.restaurant.dto.request.CreateRestaurantRequest;
import com.chatore.restaurant.dto.request.UpdateRestaurantRequest;
import com.chatore.restaurant.dto.response.RestaurantResponse;
import com.chatore.restaurant.entity.Restaurant;
import com.chatore.restaurant.enums.AvailabilityStatus;
import com.chatore.restaurant.enums.RestaurantStatus;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface RestaurantMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "ownerId", ignore = true)
    @Mapping(target = "status", constant = "PENDING")
    @Mapping(target = "availabilityStatus", constant = "CLOSED")
    @Mapping(target = "averageRating", constant = "0.0")
    @Mapping(target = "totalReviews", constant = "0")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Restaurant toEntity(CreateRestaurantRequest request);

    RestaurantResponse toResponse(Restaurant restaurant);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateRestaurant(UpdateRestaurantRequest request,
                          @MappingTarget Restaurant restaurant);

}