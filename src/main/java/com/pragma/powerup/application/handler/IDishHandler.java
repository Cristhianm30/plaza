package com.pragma.powerup.application.handler;

import com.pragma.powerup.application.dto.request.DishRequestDto;
import com.pragma.powerup.application.dto.request.DishUpdateRequestDto;
import com.pragma.powerup.application.dto.response.DishResponseDto;

public interface IDishHandler {
    DishResponseDto createDish(DishRequestDto dishRequest, String token);
    DishResponseDto updateDish(Long id, DishUpdateRequestDto dishUpdateRequest, String token);

}
