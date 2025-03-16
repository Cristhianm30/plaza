package com.pragma.powerup.application.dto.request;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderRequestDto {
    private Long restaurantId;
    private List<OrderDishRequestDto> dishes;
}
