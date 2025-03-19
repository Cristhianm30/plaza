package com.pragma.powerup.application.handler;

import com.pragma.powerup.application.dto.request.OrderRequestDto;
import com.pragma.powerup.application.dto.response.OrderResponseDto;
import com.pragma.powerup.application.dto.response.PaginationResponseDto;

public interface IOrderHandler {

    OrderResponseDto createOrder(OrderRequestDto orderRequest, String token);
    PaginationResponseDto<OrderResponseDto> getOrderByStatus(String status, int page, int size, String token);
    OrderResponseDto assignEmployeeToOrder (Long orderId, String token );
    OrderResponseDto notifyOrderReady(Long orderId,String token);
    OrderResponseDto deliverOrder(Long orderId,String otp,String token);
    OrderResponseDto deleteOrder(Long orderId,String token);

}
