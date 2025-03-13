package com.pragma.powerup.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantItemResponseDto {
    private String name;
    private String logoUrl;
}
