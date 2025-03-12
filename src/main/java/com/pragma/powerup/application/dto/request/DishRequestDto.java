package com.pragma.powerup.application.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DishRequestDto {

    private String name;
    private Integer price;
    private String description;
    private String imageUrl;
    private Long categoryId;
    private Long restaurantId;

}
