package com.pragma.powerup.domain.api;


import com.pragma.powerup.domain.model.*;

import java.util.List;

public interface IOrderServicePort {
    Order createOrder(Order order, String token);
    Pagination<Order> getAllOrdersPaginated(String status, int page, int size, String token);
    Order assignEmployeeToOrder(Long orderId, String token);
    Order notifyOrderReady(Long orderId, String token);
    Order deliverOrder(Long orderId, String otp, String token);
    Order cancelOrder(Long orderId, String token);
    List<Traceability> getTraceabilityByClient(String token);
    List<OrderEfficiency> getOrdersEfficiency(String token);
    List<EmployeeRanking> getEmployeeRanking(String token);

}
