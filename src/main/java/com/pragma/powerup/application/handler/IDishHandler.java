package com.pragma.powerup.application.handler;

import com.pragma.powerup.application.dto.request.DishActiveRequestDto;
import com.pragma.powerup.application.dto.request.DishRequestDto;
import com.pragma.powerup.application.dto.request.DishUpdateRequestDto;
import com.pragma.powerup.application.dto.response.DishResponseDto;
import com.pragma.powerup.application.dto.response.PaginationResponseDto;

public interface IDishHandler {
    DishResponseDto createDish(DishRequestDto dishRequest, String token);
    DishResponseDto updateDish(Long id, DishUpdateRequestDto dishUpdateRequest, String token);
    DishResponseDto activeDish(Long id, DishActiveRequestDto dishActiveRequestDto, String token);
    PaginationResponseDto<DishResponseDto> getAllDishesByRestaurant(Long restaurantId, Long categoryId, int page, int size, String sortBy);
}
