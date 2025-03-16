package com.pragma.powerup.domain.usecase.validations;

import com.pragma.powerup.domain.exception.DomainException;
import com.pragma.powerup.domain.exception.HasActiveOrderException;
import com.pragma.powerup.domain.exception.MultipleRestaurantsException;
import com.pragma.powerup.domain.model.Dish;
import com.pragma.powerup.domain.model.Order;
import com.pragma.powerup.domain.spi.IDishPersistencePort;
import com.pragma.powerup.domain.spi.IOrderPersistencePort;

public class OrderValidations {

    private final IOrderPersistencePort orderPersistencePort;

    public OrderValidations(IOrderPersistencePort orderPersistencePort, IDishPersistencePort dishPersistencePort) {
        this.orderPersistencePort = orderPersistencePort;
        this.dishPersistencePort = dishPersistencePort;
    }

    private final IDishPersistencePort dishPersistencePort;

    public void validateActiveOrders(Long clientId) {
        if (orderPersistencePort.existsActiveOrder(clientId)) {
            throw new HasActiveOrderException();
        }
    }

    public void validateDishesBelongToRestaurant(Order order) {
        Long restaurantId = order.getRestaurant().getId();
        order.getDishes().forEach(orderDish -> {
            Dish dish = dishPersistencePort.findById(orderDish.getDish().getId());
            if (!dish.getRestaurant().getId().equals(restaurantId)) {
                throw new MultipleRestaurantsException();
            }
        });
    }
}
