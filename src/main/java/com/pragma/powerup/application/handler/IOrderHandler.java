package com.pragma.powerup.application.handler;

import com.pragma.powerup.application.dto.request.OrderRequestDto;
import com.pragma.powerup.application.dto.response.OrderResponseDto;
import com.pragma.powerup.application.dto.response.PaginationResponseDto;
import com.pragma.powerup.domain.model.Pagination;

public interface IOrderHandler {
    OrderResponseDto createOrder(OrderRequestDto orderRequest, String token);
    PaginationResponseDto<OrderResponseDto> getOrderByStatus(String status, int page, int size, String token);
}
