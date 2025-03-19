package com.pragma.powerup.infrastructure.input.rest;

import com.pragma.powerup.application.dto.request.AssignEmployeeRequestDto;
import com.pragma.powerup.application.dto.request.RestaurantRequestDto;
import com.pragma.powerup.application.dto.response.AssignEmployeeResponseDto;
import com.pragma.powerup.application.dto.response.PaginationResponseDto;
import com.pragma.powerup.application.dto.response.RestaurantItemResponseDto;
import com.pragma.powerup.application.dto.response.RestaurantResponseDto;
import com.pragma.powerup.application.handler.impl.RestaurantHandlerImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/restaurants")
@RequiredArgsConstructor
public class RestaurantRestController {

    private final RestaurantHandlerImpl restaurantHandler;


    @PostMapping
    public ResponseEntity<RestaurantResponseDto> createRestaurant (@RequestBody RestaurantRequestDto restaurantRequestDto){
        RestaurantResponseDto createdRestaurant = restaurantHandler.createRestaurant(restaurantRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRestaurant);
    }

    @GetMapping("/list")
    public ResponseEntity<PaginationResponseDto<RestaurantItemResponseDto>> getRestaurants(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy) {

        return ResponseEntity.ok(restaurantHandler.getPaginatedRestaurants(page, size, sortBy));
    }

    @PostMapping("/{restaurantId}/employee")
    public ResponseEntity<AssignEmployeeResponseDto> assignEmployeeToRestaurant(
            @PathVariable("restaurantId") Long idRestaurant,
            @RequestBody AssignEmployeeRequestDto assignEmployeeRequestDto,
            @RequestHeader("Authorization") String token
    ){
        AssignEmployeeResponseDto response = restaurantHandler.assignEmployeeToRestaurant(idRestaurant, assignEmployeeRequestDto, token);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
