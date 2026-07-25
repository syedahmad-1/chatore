package com.chatore.restaurant.mapper;

import com.chatore.restaurant.dto.request.CreateRestaurantAddressRequest;
import com.chatore.restaurant.dto.request.UpdateRestaurantAddressRequest;
import com.chatore.restaurant.dto.response.RestaurantAddressResponse;
import com.chatore.restaurant.entity.RestaurantAddress;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface RestaurantAddressMapper {

    RestaurantAddress toEntity(CreateRestaurantAddressRequest request);

    @Mapping(target = "restaurantId", source = "restaurant.id")
    RestaurantAddressResponse toResponse(RestaurantAddress address);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateAddress(UpdateRestaurantAddressRequest request,
                       @MappingTarget RestaurantAddress address);
}