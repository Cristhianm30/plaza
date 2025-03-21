package com.pragma.powerup.infrastructure.out.feign.adapter;

import com.pragma.powerup.application.dto.response.OrderEfficiencyDto;
import com.pragma.powerup.application.dto.response.TraceabilityDto;
import com.pragma.powerup.application.mapper.IEfficiencyMapper;
import com.pragma.powerup.domain.model.OrderEfficiency;
import com.pragma.powerup.domain.model.Traceability;
import com.pragma.powerup.domain.spi.ITraceabilityFeignPort;
import com.pragma.powerup.infrastructure.out.feign.client.ITraceabilityFeignClient;
import com.pragma.powerup.application.mapper.ITraceabilityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TraceabilityFeignAdapter implements ITraceabilityFeignPort {

    private final ITraceabilityFeignClient traceabilityFeignClient;
    private final ITraceabilityMapper traceabilityMapper;
    private final IEfficiencyMapper efficiencyMapper;


    @Override
    public Traceability sendTraceability(Traceability traceability) {

        TraceabilityDto dto = traceabilityMapper.toDto(traceability);
        ResponseEntity<TraceabilityDto> response = traceabilityFeignClient.saveTraceability(dto);

        return traceabilityMapper.toModel(response.getBody());
    }

    @Override
    public List<Traceability> getTraceabilityByClient(Long clientId) {
        ResponseEntity<List<TraceabilityDto>> response = traceabilityFeignClient.getTraceabilityByClient(clientId);
        List<TraceabilityDto> dtoList = response.getBody();
        return dtoList != null ? dtoList.stream()
                .map(traceabilityMapper::toModel)
                .collect(Collectors.toList()) : null;
    }

    @Override
    public List<OrderEfficiency> getOrderEfficiency(List<Long> orderId) {

        ResponseEntity<List<OrderEfficiencyDto>> response = traceabilityFeignClient.getOrderEfficiency(orderId);
        List<OrderEfficiencyDto> dtoList = response.getBody();

        return dtoList != null ? dtoList.stream()
                .map(efficiencyMapper::toModel)
                .collect(Collectors.toList()) : null;
    }
}
