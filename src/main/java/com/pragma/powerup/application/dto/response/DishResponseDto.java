package com.pragma.powerup.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DishResponseDto {

    private Long id;
    private String name;
    private String description;
    private Integer price;
    private String imageUrl;
    private String category;
    private boolean active;

}
