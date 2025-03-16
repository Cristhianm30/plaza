package com.pragma.powerup.application.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDishRequestDto {
    private Long dishId;
    private Integer quantity;
}