package com.pragma.powerup.application.mapper;

import com.pragma.powerup.application.dto.response.RestaurantItemResponseDto;
import com.pragma.powerup.application.dto.response.RestaurantResponseDto;
import com.pragma.powerup.domain.model.Restaurant;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IRestaurantResponseMapper {

    Restaurant responseToModel(RestaurantResponseDto restaurantResponseDto);
    RestaurantResponseDto modelToResponse(Restaurant restaurant);
    RestaurantItemResponseDto toRestaurantItemDto(Restaurant restaurant);
}
