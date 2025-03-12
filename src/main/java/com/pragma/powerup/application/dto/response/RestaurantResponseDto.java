package com.pragma.powerup.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantResponseDto {
    private String name;
    private String nit;
    private String address;
    private String phone;
    private String logoUrl;
    private Long ownerId;
}
