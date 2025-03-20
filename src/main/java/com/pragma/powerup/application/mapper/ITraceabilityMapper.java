package com.pragma.powerup.application.mapper;

import com.pragma.powerup.application.dto.response.TraceabilityDto;
import com.pragma.powerup.domain.model.Traceability;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface ITraceabilityMapper {
    Traceability toModel(TraceabilityDto traceabilityDto);
    TraceabilityDto toDto(Traceability traceability);
}
