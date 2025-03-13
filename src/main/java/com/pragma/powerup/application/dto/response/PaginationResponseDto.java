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
public class PaginationResponseDto<T> {
    private List<T> items;
    private int currentPage;
    private int totalPages;
    private long totalItems;
}
