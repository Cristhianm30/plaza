package com.pragma.powerup.application.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DishUpdateRequestDto {
    private String description;
    private Integer price;
}
