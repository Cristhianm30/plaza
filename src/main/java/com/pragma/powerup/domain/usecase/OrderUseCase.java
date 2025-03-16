package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.api.IOrderServicePort;
import com.pragma.powerup.domain.model.Order;
import com.pragma.powerup.domain.spi.IOrderPersistencePort;
import com.pragma.powerup.domain.usecase.validations.OrderValidations;
import com.pragma.powerup.domain.usecase.validations.TokenValidations;

import java.time.LocalDateTime;


public class OrderUseCase implements IOrderServicePort {

    private final IOrderPersistencePort orderPersistencePort;
    private final TokenValidations tokenValidations;
    private final OrderValidations orderValidations;

    public OrderUseCase(IOrderPersistencePort orderPersistencePort, TokenValidations tokenValidations, OrderValidations orderValidations) {
        this.orderPersistencePort = orderPersistencePort;
        this.tokenValidations = tokenValidations;
        this.orderValidations = orderValidations;
    }

    @Override
    public Order createOrder(Order order, String token) {
        String cleanedToken = tokenValidations.cleanedToken(token);
        Long clientId = tokenValidations.getUserIdFromToken(cleanedToken);
        orderValidations.validateActiveOrders(clientId);
        order.setClientId(clientId);
        orderValidations.validateDishesBelongToRestaurant(order);
        order.setStatus("PENDIENTE");
        order.setDate(LocalDateTime.now());

        // Guardar pedido
        return orderPersistencePort.saveOrder(order);
    }
}
