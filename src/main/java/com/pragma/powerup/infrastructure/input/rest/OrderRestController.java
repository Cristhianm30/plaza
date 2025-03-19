package com.pragma.powerup.infrastructure.input.rest;

import com.pragma.powerup.application.dto.request.OrderRequestDto;
import com.pragma.powerup.application.dto.response.OrderResponseDto;
import com.pragma.powerup.application.dto.response.PaginationResponseDto;
import com.pragma.powerup.application.handler.IOrderHandler;
import com.pragma.powerup.domain.api.IOrderServicePort;
import com.pragma.powerup.domain.model.Pagination;
import com.pragma.powerup.domain.spi.IJwtTokenProviderPort;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderRestController {

    private final IOrderHandler orderHandler;

    @PostMapping
    public ResponseEntity<OrderResponseDto> createOrder(
            @RequestBody OrderRequestDto orderRequest,
            @RequestHeader("Authorization") String token
    ) {
        OrderResponseDto response = orderHandler.createOrder(orderRequest, token);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/list")
    public ResponseEntity<PaginationResponseDto<OrderResponseDto>> getOrderByStatus(
            @RequestParam(defaultValue = "PENDIENTE") String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestHeader("Authorization") String token
    ){
        return ResponseEntity.ok(orderHandler.getOrderByStatus(status,page,size,token));
    }

    @PatchMapping("/employee/{orderId}")
    public ResponseEntity<OrderResponseDto> assignEmployeeToOrder(
            @PathVariable("orderId") Long orderId,
            @RequestHeader("Authorization") String token
    ){
        return ResponseEntity.ok(orderHandler.assignEmployeeToOrder(orderId,token));
    }

    @PatchMapping("ready/{orderId}")
    public ResponseEntity<OrderResponseDto> notifyOrderReady(
            @PathVariable("orderId") Long orderId,
            @RequestHeader ("Authorization") String token
    ){
        return ResponseEntity.ok(orderHandler.notifyOrderReady(orderId,token));
    }


}
