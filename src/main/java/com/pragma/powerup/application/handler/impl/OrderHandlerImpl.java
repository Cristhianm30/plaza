package com.pragma.powerup.application.handler.impl;

import com.pragma.powerup.application.dto.request.OrderRequestDto;
import com.pragma.powerup.application.dto.response.OrderResponseDto;
import com.pragma.powerup.application.handler.IOrderHandler;
import com.pragma.powerup.application.mapper.IOrderRequestMapper;
import com.pragma.powerup.application.mapper.IOrderResponseMapper;
import com.pragma.powerup.domain.api.IOrderServicePort;
import com.pragma.powerup.domain.model.Order;
import com.pragma.powerup.domain.spi.IDishPersistencePort;
import com.pragma.powerup.domain.spi.IRestaurantPersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderHandlerImpl implements IOrderHandler {

    private final IOrderServicePort orderServicePort;
    private final IOrderRequestMapper orderRequestMapper;
    private final IOrderResponseMapper orderResponseMapper;
    private final IRestaurantPersistencePort restaurantPersistencePort;
    private final IDishPersistencePort dishPersistencePort;

    @Override
    public OrderResponseDto createOrder(OrderRequestDto orderRequest, String token) {

        Order order = orderRequestMapper.toOrder(orderRequest,restaurantPersistencePort,dishPersistencePort);
        Order response = orderServicePort.createOrder(order,token);
        return orderResponseMapper.toResponse(response);

    }
}
