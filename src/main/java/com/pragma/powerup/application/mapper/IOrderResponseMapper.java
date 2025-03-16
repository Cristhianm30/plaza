package com.pragma.powerup.application.mapper;

import com.pragma.powerup.application.dto.response.OrderResponseDto;
import com.pragma.powerup.domain.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IOrderResponseMapper {
    @Mapping(target = "restaurantId", source = "restaurant.id")
    OrderResponseDto toResponse(Order order);
}
