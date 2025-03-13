package com.pragma.powerup.application.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RestaurantPaginationResponseDto<T> {
    private List<T> restaurants;
    private int currentPage;
    private int totalPages;
    private long totalItems;
}
