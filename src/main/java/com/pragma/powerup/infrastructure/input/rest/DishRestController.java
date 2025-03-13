package com.pragma.powerup.infrastructure.input.rest;


import com.pragma.powerup.application.dto.request.DishActiveRequestDto;
import com.pragma.powerup.application.dto.request.DishRequestDto;
import com.pragma.powerup.application.dto.request.DishUpdateRequestDto;
import com.pragma.powerup.application.dto.response.DishResponseDto;
import com.pragma.powerup.application.dto.response.PaginationResponseDto;
import com.pragma.powerup.application.handler.IDishHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dishes")
@RequiredArgsConstructor
public class DishRestController {

    private final IDishHandler dishHandler;

    @PostMapping
    public ResponseEntity<DishResponseDto> createDish(
            @RequestBody DishRequestDto dishRequest,
            @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(dishHandler.createDish(dishRequest, token));
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<DishResponseDto> updateDish(
            @PathVariable Long id,
            @RequestBody DishUpdateRequestDto dishUpdateRequest,
            @RequestHeader("Authorization") String token) {

        return ResponseEntity.ok(dishHandler.updateDish(id, dishUpdateRequest, token));
    }

    @PatchMapping("/active/{id}")
    public ResponseEntity<DishResponseDto> activeDish(
            @PathVariable Long id,
            @RequestBody DishActiveRequestDto dishActiveRequestDto,
            @RequestHeader("Authorization") String token){

        return ResponseEntity.ok(dishHandler.activeDish(id, dishActiveRequestDto, token));
    }

    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<PaginationResponseDto<DishResponseDto>> getAllDishesByRestaurant(
            @PathVariable Long restaurantId,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy) {

        return ResponseEntity.ok(dishHandler.getAllDishesByRestaurant(restaurantId, categoryId, page, size, sortBy));
    }


}
