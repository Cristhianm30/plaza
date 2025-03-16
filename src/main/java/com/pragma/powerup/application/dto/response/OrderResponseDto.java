package com.pragma.powerup.application.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponseDto {
    private Long id;
    private Long restaurantId;
    private Long chefId;
    private String status;
    private LocalDateTime date;
}
