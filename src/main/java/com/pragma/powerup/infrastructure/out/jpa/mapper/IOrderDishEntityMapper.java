package com.pragma.powerup.infrastructure.out.jpa.mapper;

import com.pragma.powerup.domain.model.OrderDish;
import com.pragma.powerup.infrastructure.out.jpa.entity.OrderDishEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IOrderDishEntityMapper {

    @Mapping(target = "dishId", source = "dish.id") // Mapea el ID del Dish al dishId
    OrderDishEntity toEntity(OrderDish orderDish);

    @Mapping(target = "dish.id", source = "dishId") // Opcional, si necesitas el mapeo inverso
    OrderDish toModel(OrderDishEntity entity);

}
