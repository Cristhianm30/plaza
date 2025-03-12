package com.pragma.powerup.infrastructure.input.rest;

import com.pragma.powerup.application.dto.request.RestaurantRequestDto;
import com.pragma.powerup.application.dto.response.RestaurantResponseDto;
import com.pragma.powerup.application.handler.impl.RestaurantHandlerImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/plaza")
@RequiredArgsConstructor
public class RestaurantRestController {

    private final RestaurantHandlerImpl restaurantHandler;


    @PostMapping("restaurant")
    public ResponseEntity<RestaurantResponseDto> createRestaurant (@RequestBody RestaurantRequestDto restaurantRequestDto){
        RestaurantResponseDto createdRestaurant = restaurantHandler.createRestaurant(restaurantRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRestaurant);
    }
}
