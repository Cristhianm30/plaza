package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.api.IOrderServicePort;
import com.pragma.powerup.domain.exception.OrdersNotFoundException;
import com.pragma.powerup.domain.model.*;
import com.pragma.powerup.domain.spi.*;
import com.pragma.powerup.domain.usecase.validations.OrderValidations;
import com.pragma.powerup.domain.usecase.validations.TokenValidations;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Builder
public class OrderUseCase implements IOrderServicePort {

    private final IOrderPersistencePort orderPersistencePort;
    private final TokenValidations tokenValidations;
    private final OrderValidations orderValidations;
    private final IUserFeignPort userFeignPort;
    private final IEmployeeRestaurantPersistencePort employeeRestaurantPersistencePort;
    private final IMessagingFeignPort messagingFeignPort;
    private final IOrderOtpPersistencePort orderOtpPersistencePort;
    private final ITraceabilityFeignPort traceabilityFeignPort;
    private final IRestaurantPersistencePort restaurantPersistencePort;


    @Override
    public Order createOrder(Order order, String token) {

        String cleanedToken = tokenValidations.cleanedToken(token);
        Long clientId = tokenValidations.getUserIdFromToken(cleanedToken);

        orderValidations.validateActiveOrders(clientId);
        order.setClientId(clientId);
        orderValidations.validateDishesBelongToRestaurant(order);

        order.setStatus("PENDIENTE");
        order.setDate(LocalDateTime.now());
        String userEmail = userFeignPort.getUserEmail(clientId);

        Order savedOrder = orderPersistencePort.saveOrder(order);
        orderValidations.createOrCancelTraceability(savedOrder,userEmail);
        return savedOrder;
    }

    @Override
    public Pagination<Order> getAllOrdersPaginated(String status, int page, int size, String token) {

        String cleanedToken = tokenValidations.cleanedToken(token);
        Long employeeId = tokenValidations.getUserIdFromToken(cleanedToken);

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


        String clientEmail = userFeignPort.getUserEmail(order.getClientId());
        String employeeEmail = userFeignPort.getUserEmail(order.getChefId());


        Order savedOrder = orderPersistencePort.saveOrder(order);
        orderValidations.updateTraceability(savedOrder,clientEmail,employeeEmail);
        return savedOrder;

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

        String clientEmail = userFeignPort.getUserEmail(order.getClientId());
        String employeeEmail = userFeignPort.getUserEmail(order.getChefId());

        Order savedOrder = orderPersistencePort.saveOrder(order);
        orderValidations.updateTraceability(savedOrder,clientEmail,employeeEmail);

        return savedOrder;
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

        String clientEmail = userFeignPort.getUserEmail(order.getClientId());
        String employeeEmail = userFeignPort.getUserEmail(order.getChefId());

        Order savedOrder = orderPersistencePort.saveOrder(order);
        orderValidations.updateTraceability(savedOrder,clientEmail,employeeEmail);

        return savedOrder;
    }

    @Override
    public Order cancelOrder(Long orderId, String token) {

        String cleanedToken = tokenValidations.cleanedToken(token);

        Long clientId = tokenValidations.getUserIdFromToken(cleanedToken);

        Order order = orderPersistencePort.findById(orderId);
        orderValidations.validateToCancel(order);
        orderValidations.validateOrderClient(clientId,order);

        order.setStatus("CANCELADO");

        String userEmail = userFeignPort.getUserEmail(clientId);

        Order savedOrder = orderPersistencePort.saveOrder(order);
        orderValidations.createOrCancelTraceability(savedOrder,userEmail);
        return savedOrder;
    }

    @Override
    public List<Traceability> getTraceabilityByClient(String token) {

        String cleanedToken = tokenValidations.cleanedToken(token);

        Long clientId = tokenValidations.getUserIdFromToken(cleanedToken);

        return traceabilityFeignPort.getTraceabilityByClient(clientId);
    }

    @Override
    public List<OrderEfficiency> getOrdersEfficiency(String token) {

        String cleanedToken = tokenValidations.cleanedToken(token);

        Long ownerId = tokenValidations.getUserIdFromToken(cleanedToken);

        Restaurant ownerRestaurant = restaurantPersistencePort.findRestaurantByOwnerId(ownerId);

        Long restaurantId = ownerRestaurant.getId();

        List<Order> orders = orderPersistencePort.findOrdersByRestaurantId(restaurantId);

        if (orders.isEmpty()) {
            throw new OrdersNotFoundException();
        }

        List<Long> orderIds = orders.stream()
                .map(Order::getId)
                .collect(Collectors.toList());


        return traceabilityFeignPort.getOrderEfficiency(orderIds);
    }

    @Override
    public List<EmployeeRanking> getEmployeeRanking(String token) {

        String cleanedToken = tokenValidations.cleanedToken(token);

        Long ownerId = tokenValidations.getUserIdFromToken(cleanedToken);

        Restaurant ownerRestaurant = restaurantPersistencePort.findRestaurantByOwnerId(ownerId);

        Long restaurantId = ownerRestaurant.getId();

        List<Order> orders = orderPersistencePort.findOrdersByRestaurantId(restaurantId);

        if (orders.isEmpty()) {
            throw new OrdersNotFoundException();
        }

        List<Long> orderIds = orders.stream()
                .map(Order::getId)
                .collect(Collectors.toList());

        return traceabilityFeignPort.getEmployeeRanking(orderIds);
    }


}
