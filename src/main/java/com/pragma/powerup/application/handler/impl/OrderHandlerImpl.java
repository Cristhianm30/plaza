package com.pragma.powerup.application.handler.impl;

import com.pragma.powerup.application.dto.request.OrderRequestDto;
import com.pragma.powerup.application.dto.response.OrderResponseDto;
import com.pragma.powerup.application.dto.response.PaginationResponseDto;
import com.pragma.powerup.application.dto.response.TraceabilityDto;
import com.pragma.powerup.application.handler.IOrderHandler;
import com.pragma.powerup.application.mapper.IOrderRequestMapper;
import com.pragma.powerup.application.mapper.IOrderResponseMapper;
import com.pragma.powerup.application.mapper.ITraceabilityMapper;
import com.pragma.powerup.domain.api.IOrderServicePort;
import com.pragma.powerup.domain.model.Order;
import com.pragma.powerup.domain.model.Pagination;
import com.pragma.powerup.domain.model.Traceability;
import com.pragma.powerup.domain.spi.IDishPersistencePort;
import com.pragma.powerup.domain.spi.IRestaurantPersistencePort;
import com.pragma.powerup.domain.spi.ITraceabilityFeignPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderHandlerImpl implements IOrderHandler {

    private final IOrderServicePort orderServicePort;
    private final IOrderRequestMapper orderRequestMapper;
    private final IOrderResponseMapper orderResponseMapper;
    private final IRestaurantPersistencePort restaurantPersistencePort;
    private final IDishPersistencePort dishPersistencePort;
    private final ITraceabilityMapper traceabilityMapper;

    @Override
    public OrderResponseDto createOrder(OrderRequestDto orderRequest, String token) {

        Order order = orderRequestMapper.toOrder(orderRequest,restaurantPersistencePort,dishPersistencePort);
        Order response = orderServicePort.createOrder(order,token);
        return orderResponseMapper.toResponse(response);

    }

    @Override
    public PaginationResponseDto<OrderResponseDto> getOrderByStatus(String status, int page, int size, String token) {

        Pagination<Order> pagination = orderServicePort.getAllOrdersPaginated(status, page,size,token);

        return PaginationResponseDto.<OrderResponseDto>builder()
                .items(
                        pagination.getItems().stream()
                                .map(orderResponseMapper::toResponse)
                                .collect(Collectors.toList())
                )
                .currentPage(pagination.getCurrentPage())
                .totalPages(pagination.getTotalPages())
                .totalItems(pagination.getTotalItems())
                .build();
    }

    @Override
    public OrderResponseDto assignEmployeeToOrder(Long orderId, String token) {

        Order assign = orderServicePort.assignEmployeeToOrder(orderId,token);

        return orderResponseMapper.toResponse(assign);
    }

    @Override
    public OrderResponseDto notifyOrderReady(Long orderId, String token) {

        Order updatedOrder = orderServicePort.notifyOrderReady(orderId,token);

        return orderResponseMapper.toResponse(updatedOrder);
    }

    @Override
    public OrderResponseDto deliverOrder(Long orderId, String otp, String token) {

        Order updatedOrder = orderServicePort.deliverOrder(orderId,otp,token);

        return orderResponseMapper.toResponse(updatedOrder);
    }

    @Override
    public OrderResponseDto cancelOrder(Long orderId, String token) {

        Order deletedOrder = orderServicePort.cancelOrder(orderId,token);

        return orderResponseMapper.toResponse(deletedOrder);
    }

    @Override
    public List<TraceabilityDto> getLogsByClient(String token) {

        List<Traceability> traceList = orderServicePort.getTraceabilityByClient(token);

        return traceList.stream()
                .map(traceabilityMapper::toDto)
                .collect(Collectors.toList());
    }


}
