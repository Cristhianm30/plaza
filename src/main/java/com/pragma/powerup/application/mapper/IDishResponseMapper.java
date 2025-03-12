package com.pragma.powerup.application.mapper;


import com.pragma.powerup.application.dto.response.DishResponseDto;
import com.pragma.powerup.domain.model.Dish;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IDishResponseMapper {

    @Mapping(target = "category", source = "category.name")
    DishResponseDto modelToResponse(Dish dish);

}
