package com.pragma.powerup.infrastructure.out.jpa.mapper;

import com.pragma.powerup.domain.model.Order;
import com.pragma.powerup.domain.model.OrderDish;
import com.pragma.powerup.infrastructure.out.jpa.entity.OrderDishEntity;
import com.pragma.powerup.infrastructure.out.jpa.entity.OrderEntity;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring",
        uses = {IOrderDishEntityMapper.class},
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IOrderEntityMapper {
    @Mapping(target = "dishes", source = "dishes")
    OrderEntity toEntity(Order order);

    @Mapping(target = "dishes", source = "dishes")
    Order toModel(OrderEntity entity);


    default List<OrderDishEntity> mapDishes(List<OrderDish> dishes, @Context OrderEntity orderEntity) {
        return dishes.stream()
                .map(dish -> {
                    OrderDishEntity entity = new OrderDishEntity();
                    entity.setOrder(orderEntity); // Asignar la OrderEntity
                    entity.setDishId(dish.getDish().getId());
                    entity.setQuantity(dish.getQuantity());
                    return entity;
                })
                .collect(Collectors.toList());
    }
}
