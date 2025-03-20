package com.pragma.powerup.domain.usecase.validations;

import com.pragma.powerup.domain.exception.*;
import com.pragma.powerup.domain.model.Dish;
import com.pragma.powerup.domain.model.Order;
import com.pragma.powerup.domain.model.OrderOtp;
import com.pragma.powerup.domain.model.Traceability;
import com.pragma.powerup.domain.spi.IDishPersistencePort;
import com.pragma.powerup.domain.spi.IOrderPersistencePort;
import com.pragma.powerup.domain.spi.ITraceabilityFeignPort;

public class OrderValidations {

    private final IOrderPersistencePort orderPersistencePort;
    private final IDishPersistencePort dishPersistencePort;
    private final ITraceabilityFeignPort traceabilityFeignPort;

    public OrderValidations(IOrderPersistencePort orderPersistencePort, IDishPersistencePort dishPersistencePort, ITraceabilityFeignPort traceabilityFeignPort) {
        this.orderPersistencePort = orderPersistencePort;
        this.dishPersistencePort = dishPersistencePort;
        this.traceabilityFeignPort = traceabilityFeignPort;
    }



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

    public void validateOrderClient (Long clientId,Order order){
        if(!order.getClientId().equals(clientId)){
            throw new InvalidOrderClientException();
        }
    }

    public void validateToCancel(Order order){
        if (!order.getStatus().equals("PENDIENTE")){
            throw  new InvalidCancelingException();
        }
    }

    public void createTraceability(Order order,String userEmail){
        Traceability traceability = new Traceability();
        traceability.setOrderId(order.getId());
        traceability.setClientId(order.getClientId());
        traceability.setClientEmail(userEmail);
        traceability.setDate(order.getDate());
        traceability.setLastStatus(null);
        traceability.setNewStatus(order.getStatus());
        traceability.setEmployeeId(null);
        traceability.setEmployeeEmail(null);

        traceabilityFeignPort.sendTraceability(traceability);

    }


}
