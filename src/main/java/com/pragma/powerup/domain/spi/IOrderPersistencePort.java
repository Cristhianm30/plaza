package com.pragma.powerup.domain.spi;

import com.pragma.powerup.domain.model.Order;

public interface IOrderPersistencePort {
    Order saveOrder(Order order);
    boolean existsActiveOrder(Long clientId);
}
