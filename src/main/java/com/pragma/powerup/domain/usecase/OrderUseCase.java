package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.api.IOrderServicePort;
import com.pragma.powerup.domain.model.EmployeeRestaurant;
import com.pragma.powerup.domain.model.Order;
import com.pragma.powerup.domain.model.Pagination;
import com.pragma.powerup.domain.spi.IEmployeeRestaurantPersistencePort;
import com.pragma.powerup.domain.spi.IOrderPersistencePort;
import com.pragma.powerup.domain.spi.IUserFeignPort;
import com.pragma.powerup.domain.usecase.validations.OrderValidations;
import com.pragma.powerup.domain.usecase.validations.TokenValidations;

import java.time.LocalDateTime;


public class OrderUseCase implements IOrderServicePort {

    private final IOrderPersistencePort orderPersistencePort;
    private final TokenValidations tokenValidations;
    private final OrderValidations orderValidations;
    private final IUserFeignPort userFeignPort;
    private final IEmployeeRestaurantPersistencePort employeeRestaurantPersistencePort;

    public OrderUseCase(IOrderPersistencePort orderPersistencePort,
                        TokenValidations tokenValidations,
                        OrderValidations orderValidations,
                        IUserFeignPort userFeignPort,
                        IEmployeeRestaurantPersistencePort employeeRestaurantPersistencePort) {

        this.orderPersistencePort = orderPersistencePort;
        this.tokenValidations = tokenValidations;
        this.orderValidations = orderValidations;
        this.userFeignPort = userFeignPort;
        this.employeeRestaurantPersistencePort = employeeRestaurantPersistencePort;
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

    @Override
    public Pagination<Order> getAllOrdersPaginated(String status, int page, int size, String token) {

        String cleanedToken = tokenValidations.cleanedToken(token);
        Long employeeId = tokenValidations.getUserIdFromToken(cleanedToken);
        String userRole = userFeignPort.getUserRole(employeeId);
        orderValidations.validateEmployeeRole(userRole);
        EmployeeRestaurant employee = employeeRestaurantPersistencePort.findByEmployeeId(employeeId);
        Long restaurantId = employee.getRestaurantId();

        return orderPersistencePort.getOrdersByStatusAndRestaurant(status,restaurantId,page,size);
    }
}
