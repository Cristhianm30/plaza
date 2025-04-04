package com.pragma.powerup.domain.usecase.validations;

import com.pragma.powerup.domain.exception.*;
import com.pragma.powerup.domain.model.Dish;
import com.pragma.powerup.domain.model.Order;
import com.pragma.powerup.domain.model.OrderOtp;
import com.pragma.powerup.domain.model.Traceability;
import com.pragma.powerup.domain.spi.IDishPersistencePort;
import com.pragma.powerup.domain.spi.IOrderPersistencePort;
import com.pragma.powerup.domain.spi.ITraceabilityFeignPort;

import java.time.LocalDateTime;

public class OrderValidations {

    private final IOrderPersistencePort orderPersistencePort;
    private final IDishPersistencePort dishPersistencePort;
    private final ITraceabilityFeignPort traceabilityFeignPort;

    private static final String PENDING = "PENDIENTE";
    private static final String IN_PREPARATION = "EN_PREPARACION";
    private static final String READY = "LISTO";
    private static final String DELIVERED = "ENTREGADO";
    private static final String CANCELLED = "CANCELADO";


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
        if (!order.getStatus().equals(IN_PREPARATION)){
            throw  new NotPreparationException();
        }
    }

    public void validatePending(Order order){
        if (!order.getStatus().equals(PENDING)){
            throw  new NotPendingException();
        }
    }

    public void validateReady (Order order){
        if (!order.getStatus().equals(READY)){
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
        if (!order.getStatus().equals(PENDING)){
            throw  new InvalidCancelingException();
        }
    }

    public void createOrCancelTraceability(Order order, String userEmail){
        Traceability traceability = new Traceability();
        traceability.setOrderId(order.getId());
        traceability.setClientId(order.getClientId());
        traceability.setClientEmail(userEmail);
        LocalDateTime now = LocalDateTime.now();
        traceability.setDate(now);
        traceability.setLastStatus(lastStatus(order));
        traceability.setNewStatus(order.getStatus());
        traceability.setEmployeeId(null);
        traceability.setEmployeeEmail(null);

        traceabilityFeignPort.sendTraceability(traceability);

    }

    public void updateTraceability (Order order, String clientEmail,String employeeEmail){
        Traceability traceability = new Traceability();
        traceability.setOrderId(order.getId());
        traceability.setClientId(order.getClientId());
        traceability.setClientEmail(clientEmail);
        LocalDateTime now = LocalDateTime.now();
        traceability.setDate(now);
        traceability.setLastStatus(lastStatus(order));
        traceability.setNewStatus(order.getStatus());
        traceability.setEmployeeId(order.getChefId());
        traceability.setEmployeeEmail(employeeEmail);

        traceabilityFeignPort.sendTraceability(traceability);
    }



    private String lastStatus (Order order){
        String lastStatus;
        if (order.getStatus() != null) {
            switch (order.getStatus()) {
                case PENDING:
                    lastStatus = null;
                    break;
                case IN_PREPARATION:
                case CANCELLED:
                    lastStatus = PENDING;
                    break;
                case READY:
                    lastStatus = IN_PREPARATION;
                    break;
                case DELIVERED:
                    lastStatus = READY;
                    break;
                default:
                    throw new InvalidOrderStatusException();
            }
            return lastStatus;
        } else {
           throw new InvalidOrderStatusException();
        }
    }


}
