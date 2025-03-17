package com.pragma.powerup.application.handler;

import com.pragma.powerup.application.dto.request.AssignEmployeeRequestDto;
import com.pragma.powerup.application.dto.request.RestaurantRequestDto;
import com.pragma.powerup.application.dto.response.AssignEmployeeResponseDto;
import com.pragma.powerup.application.dto.response.PaginationResponseDto;
import com.pragma.powerup.application.dto.response.RestaurantItemResponseDto;
import com.pragma.powerup.application.dto.response.RestaurantResponseDto;

public interface IRestaurantHandler {

    RestaurantResponseDto createRestaurant (RestaurantRequestDto restaurantRequestDto);
    PaginationResponseDto<RestaurantItemResponseDto> getPaginatedRestaurants(int page, int size, String sortBy);
    AssignEmployeeResponseDto assignEmployeeToRestaurant(Long idRestaurant, AssignEmployeeRequestDto assignEmployeeRequestDto, String token);
}
