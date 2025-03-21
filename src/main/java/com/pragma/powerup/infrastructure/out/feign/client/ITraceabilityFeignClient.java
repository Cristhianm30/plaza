package com.pragma.powerup.infrastructure.out.feign.client;

import com.pragma.powerup.application.dto.response.EmployeeRankingDto;
import com.pragma.powerup.application.dto.response.OrderEfficiencyDto;
import com.pragma.powerup.application.dto.response.TraceabilityDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "traceability-service", url = "${traceability-service.url}")
public interface ITraceabilityFeignClient {

    @PostMapping("/traceability")
    ResponseEntity<TraceabilityDto> saveTraceability(TraceabilityDto traceabilityDto);

    @GetMapping("/traceability/{clientId}")
    ResponseEntity<List<TraceabilityDto>> getTraceabilityByClient(@PathVariable("clientId") Long clientId);

    @PostMapping("/traceability/efficiency/orders")
    ResponseEntity<List<OrderEfficiencyDto>> getOrderEfficiency (@RequestBody List<Long> orderId);

    @PostMapping("/traceability/ranking/employees")
    ResponseEntity<List<EmployeeRankingDto>> getEmployeeRanking (@RequestBody List<Long> orderId);

}
