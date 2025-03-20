package com.pragma.powerup.infrastructure.out.feign;

import com.pragma.powerup.application.dto.response.TraceabilityDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "traceability-service", url = "${traceability-service.url}")
public interface ITraceabilityClient {

    @PostMapping("/traceability")
    ResponseEntity<TraceabilityDto> saveTraceability(TraceabilityDto traceabilityDto);

}
