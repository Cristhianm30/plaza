package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.api.IOrderServicePort;
import com.pragma.powerup.domain.model.EmployeeRestaurant;
import com.pragma.powerup.domain.model.Order;
import com.pragma.powerup.domain.model.OrderOtp;
import com.pragma.powerup.domain.model.Pagination;
import com.pragma.powerup.domain.spi.*;
import com.pragma.powerup.domain.usecase.validations.OrderValidations;
import com.pragma.powerup.domain.usecase.validations.TokenValidations;

import java.time.LocalDateTime;


public class OrderUseCase implements IOrderServicePort {

    private final IOrderPersistencePort orderPersistencePort;
    private final TokenValidations tokenValidations;
    private final OrderValidations orderValidations;
    private final IUserFeignPort userFeignPort;
    private final IEmployeeRestaurantPersistencePort employeeRestaurantPersistencePort;
    private final IMessagingFeignPort messagingFeignPort;
    private final IOrderOtpPersistencePort orderOtpPersistencePort;

    public OrderUseCase(IOrderPersistencePort orderPersistencePort,
                        TokenValidations tokenValidations,
                        OrderValidations orderValidations,
                        IUserFeignPort userFeignPort,
                        IEmployeeRestaurantPersistencePort employeeRestaurantPersistencePort,
                        IMessagingFeignPort messagingFeignPort,
                        IOrderOtpPersistencePort orderOtpPersistencePort) {

        this.orderPersistencePort = orderPersistencePort;
        this.tokenValidations = tokenValidations;
        this.orderValidations = orderValidations;
        this.userFeignPort = userFeignPort;
        this.employeeRestaurantPersistencePort = employeeRestaurantPersistencePort;
        this.messagingFeignPort = messagingFeignPort;
        this.orderOtpPersistencePort = orderOtpPersistencePort;
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
        orderValidations.validateEmployeeRole(userRole); //validacion innecesaria por el springSecurity :/

        EmployeeRestaurant employee = employeeRestaurantPersistencePort.findByEmployeeId(employeeId);
        Long restaurantId = employee.getRestaurantId();

        return orderPersistencePort.getOrdersByStatusAndRestaurant(status,restaurantId,page,size);
    }

    @Override
    public Order assignEmployeeToOrder(Long orderId, String token) {

        String cleanedToken = tokenValidations.cleanedToken(token);
        Long employeeId = tokenValidations.getUserIdFromToken(cleanedToken);

        Order order = orderPersistencePort.findById(orderId);

        orderValidations.validatePending(order);

        Long restaurantId = order.getRestaurant().getId();
        EmployeeRestaurant employee = employeeRestaurantPersistencePort.findByEmployeeId(employeeId);
        Long employeeRestaurantId = employee.getRestaurantId();
        orderValidations.validateRestaurantEmployee(restaurantId,employeeRestaurantId);

        

        order.setStatus("EN_PREPARACION");
        order.setChefId(employeeId);

        return orderPersistencePort.saveOrder(order);

    }

    @Override
    public Order notifyOrderReady(Long orderId, String token) {

        String cleanedToken = tokenValidations.cleanedToken(token);

        Long employeeId = tokenValidations.getUserIdFromToken(cleanedToken);

        Order order = orderPersistencePort.findById(orderId);
        orderValidations.validateInPreparation(order);
        orderValidations.validateOrderEmployee(employeeId,order);

        String clientPhone = userFeignPort.getUserPhone(order.getClientId());
        OrderOtp orderOtp = messagingFeignPort.sendOtp(clientPhone, order.getId());

        orderOtpPersistencePort.saveOrderOtp(orderOtp);

        order.setStatus("LISTO");

        return orderPersistencePort.saveOrder(order);
    }

    @Override
    public Order deliverOrder(Long orderId, String otp, String token) {

        String cleanedToken = tokenValidations.cleanedToken(token);

        Long employeeId = tokenValidations.getUserIdFromToken(cleanedToken);

        Order order = orderPersistencePort.findById(orderId);
        orderValidations.validateReady(order);
        orderValidations.validateOrderEmployee(employeeId,order);

        OrderOtp orderOtp = orderOtpPersistencePort.findByOrderId(orderId);
        orderValidations.validateOtp(orderOtp,otp);

        order.setStatus("ENTREGADO");

        return orderPersistencePort.saveOrder(order);
    }
}
