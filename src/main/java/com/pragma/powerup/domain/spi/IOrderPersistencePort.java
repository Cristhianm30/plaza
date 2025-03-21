package com.pragma.powerup.domain.spi;

import com.pragma.powerup.domain.model.Order;
import com.pragma.powerup.domain.model.Pagination;

import java.util.List;

public interface IOrderPersistencePort {

    Order saveOrder(Order order);

    boolean existsActiveOrder(Long clientId);

    Pagination<Order> getOrdersByStatusAndRestaurant(String status, Long restaurantId, int page, int size);

    Order findById (Long id);

    List<Order> findOrdersByRestaurantId(Long restaurantId);

}
