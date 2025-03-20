package com.pragma.powerup.infrastructure.out.feign.adapter;

import com.pragma.powerup.application.dto.response.TraceabilityDto;
import com.pragma.powerup.domain.model.Traceability;
import com.pragma.powerup.domain.spi.ITraceabilityFeignPort;
import com.pragma.powerup.infrastructure.out.feign.client.ITraceabilityFeignClient;
import com.pragma.powerup.infrastructure.out.feign.mapper.ITraceabilityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TraceabilityFeignAdapter implements ITraceabilityFeignPort {

    private final ITraceabilityFeignClient traceabilityFeignClient;
    private final ITraceabilityMapper traceabilityMapper;


    @Override
    public Traceability sendTraceability(Traceability traceability) {

        TraceabilityDto dto = traceabilityMapper.toDto(traceability);
        ResponseEntity<TraceabilityDto> response = traceabilityFeignClient.saveTraceability(dto);

        return traceabilityMapper.toModel(response.getBody());
    }
}
