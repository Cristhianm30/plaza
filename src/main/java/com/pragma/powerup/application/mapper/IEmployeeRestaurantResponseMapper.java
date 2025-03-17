package com.pragma.powerup.application.mapper;

import com.pragma.powerup.application.dto.response.AssignEmployeeResponseDto;
import com.pragma.powerup.domain.model.EmployeeRestaurant;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IEmployeeRestaurantResponseMapper {

    AssignEmployeeResponseDto modelToResponse(EmployeeRestaurant employeeRestaurant);
}
