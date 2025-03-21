package com.pragma.powerup.infrastructure.input.rest;

import com.pragma.powerup.application.dto.request.OrderRequestDto;
import com.pragma.powerup.application.dto.response.*;
import com.pragma.powerup.application.handler.IOrderHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PatchMapping("/ready/{orderId}")
    public ResponseEntity<OrderResponseDto> notifyOrderReady(
            @PathVariable("orderId") Long orderId,
            @RequestHeader ("Authorization") String token
    ){
        return ResponseEntity.ok(orderHandler.notifyOrderReady(orderId,token));
    }

    @PatchMapping("/deliver/{orderId}")
    public ResponseEntity<OrderResponseDto> deliverOrder(
            @PathVariable("orderId") Long orderId,
            @RequestParam() String otp,
            @RequestHeader ("Authorization") String token
    ){
        return ResponseEntity.ok(orderHandler.deliverOrder(orderId,otp,token));
    }

    @PatchMapping("/cancel/{orderId}")
    public ResponseEntity<OrderResponseDto> cancelOrder(
            @PathVariable("orderId") Long orderId,
            @RequestHeader ("Authorization") String token
    ){
        return ResponseEntity.ok(orderHandler.cancelOrder(orderId,token));
    }

    @GetMapping("/logs/client")
    public ResponseEntity<List<TraceabilityDto>> getTraceabilityByClient(
            @RequestHeader("Authorization") String token
    ){
        return ResponseEntity.ok(orderHandler.getLogsByClient(token));
    }

    @GetMapping("/efficiency")
    public ResponseEntity<List<OrderEfficiencyDto>> getOrdersEfficiency(
            @RequestHeader("Authorization") String token
    ) {
        List<OrderEfficiencyDto> efficiencyList = orderHandler.getOrdersEfficiency(token);
        return ResponseEntity.ok(efficiencyList);
    }

    @GetMapping("/ranking")
    public ResponseEntity<List<EmployeeRankingDto>> getEmployeeRanking(
            @RequestHeader("Authorization") String token
    ){
        List<EmployeeRankingDto> rankingList = orderHandler.getEmployeeRanking(token);
        return ResponseEntity.ok(rankingList);
    }






}
