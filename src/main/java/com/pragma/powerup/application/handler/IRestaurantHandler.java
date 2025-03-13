package com.pragma.powerup.application.handler;

import com.pragma.powerup.application.dto.request.RestaurantRequestDto;
import com.pragma.powerup.application.dto.response.RestaurantPaginationResponseDto;
import com.pragma.powerup.application.dto.response.RestaurantResponseDto;
import com.pragma.powerup.domain.model.RestaurantPagination;

public interface IRestaurantHandler {

    RestaurantResponseDto createRestaurant (RestaurantRequestDto restaurantRequestDto);
    RestaurantPaginationResponseDto getPaginatedRestaurants(int page, int size, String sortBy);
}
