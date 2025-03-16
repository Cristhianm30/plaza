package com.pragma.powerup.application.mapper;

import com.pragma.powerup.application.dto.request.OrderDishRequestDto;
import com.pragma.powerup.application.dto.request.OrderRequestDto;
import com.pragma.powerup.domain.model.Dish;
import com.pragma.powerup.domain.model.Order;
import com.pragma.powerup.domain.model.OrderDish;
import com.pragma.powerup.domain.model.Restaurant;
import com.pragma.powerup.domain.spi.IDishPersistencePort;
import com.pragma.powerup.domain.spi.IRestaurantPersistencePort;
import org.mapstruct.*;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(
        componentModel = "spring",
        uses = {IDishPersistencePort.class}, // Solo si es necesario
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface IOrderRequestMapper {

    @Mapping(
            target = "dishes",
            source = "dishes",
            qualifiedByName = "mapDishes" // Nombre debe coincidir
    )
    @Mapping(
            target = "restaurant",
            source = "restaurantId",
            qualifiedByName = "mapRestaurant" // Usar qualifier explícito
    )
    Order toOrder(
            OrderRequestDto orderRequestDto,
            @Context IRestaurantPersistencePort restaurantPort,
            @Context IDishPersistencePort dishPort
    );

    // Mapeo de dishes (corregir anotaciones)
    @Named("mapDishes")
    @IterableMapping(qualifiedByName = "mapSingleDish")
    default List<OrderDish> mapDishes(
            List<OrderDishRequestDto> dishes,
            @Context IDishPersistencePort dishPort
    ) {
        return dishes.stream()
                .map(dto -> mapSingleDish(dto, dishPort))
                .collect(Collectors.toList());
    }

    @Named("mapSingleDish")
    default OrderDish mapSingleDish(
            OrderDishRequestDto dto,
            @Context IDishPersistencePort dishPort
    ) {
        Dish dish = dishPort.findById(dto.getDishId());
        return new OrderDish(null, dish, dto.getQuantity());
    }

    // Mapeo de restaurant (evitar ambigüedad)
    @Named("mapRestaurant")
    default Restaurant mapRestaurant(
            Long restaurantId,
            @Context IRestaurantPersistencePort restaurantPort
    ) {
        return restaurantPort.findById(restaurantId);
    }
}