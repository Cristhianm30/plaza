package com.pragma.powerup.application.dto.response;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class OrderResponseDto {
    private Long id;
    private Long restaurantId;
    private String status;
    private LocalDateTime date;
}
