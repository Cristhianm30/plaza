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
        return ResponseEntity.ok(orderHandler.createOrder(orderRequest, token));
    }

    @GetMapping("/list")
    public ResponseEntity<PaginationResponseDto<OrderResponseDto>> getOrderByStatus(
            @RequestParam String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestHeader("Authorization") String token
    ){
        return ResponseEntity.ok(orderHandler.getOrderByStatus(status,page,size,token));
    }
}
