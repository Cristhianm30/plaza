package com.pragma.powerup.application.dto.request;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDishRequestDto {
    private Long dishId;
    private Integer quantity;
}