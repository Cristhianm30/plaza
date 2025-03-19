package com.pragma.powerup.domain.usecase.validations;

import com.pragma.powerup.domain.exception.*;
import com.pragma.powerup.domain.model.Dish;
import com.pragma.powerup.domain.model.Order;
import com.pragma.powerup.domain.model.OrderOtp;
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

    public void validateEmployeeRole(String role) {
        if (role == null || !role.equals("EMPLEADO")) {
            throw new InvalidEmployeeException();
        }
    }

    public void validateRestaurantEmployee (Long restaurantId,Long employeeRestaurantId){
        if (!restaurantId.equals(employeeRestaurantId)){
            throw new InvalidRestaurantEmployeeException();
        }
    }

    public void validateOrderEmployee (Long employeeId,Order order){
        if(!order.getChefId().equals(employeeId)){
            throw  new InvalidOrderEmployeeException();
        }
    }

    public void validateInPreparation(Order order){
        if (!order.getStatus().equals("EN_PREPARACION")){
            throw  new NotPreparationException();
        }
    }

    public void validatePending(Order order){
        if (!order.getStatus().equals("PENDIENTE")){
            throw  new NotPendingException();
        }
    }

    public void validateReady (Order order){
        if (!order.getStatus().equals("LISTO")){
            throw  new NotReadyException();
        }
    }

    public void validateOtp(OrderOtp orderOtp, String otp){
        if (!orderOtp.getOtp().equals(otp)){
            throw  new WrongOtpException();
        }
    }

}
