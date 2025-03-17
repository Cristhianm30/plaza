package com.pragma.powerup.infrastructure.out.jpa.mapper;

import com.pragma.powerup.domain.model.EmployeeRestaurant;
import com.pragma.powerup.infrastructure.out.jpa.entity.EmployeeRestaurantEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IEmployeeRestaurantEntityMapper {

    EmployeeRestaurantEntity modelToEntity (EmployeeRestaurant employeeRestaurant);

    EmployeeRestaurant entityToModel (EmployeeRestaurantEntity employeeRestaurantEntity);

}
