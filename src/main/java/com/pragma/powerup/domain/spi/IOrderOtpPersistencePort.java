package com.pragma.powerup.domain.spi;

import com.pragma.powerup.domain.model.OrderOtp;

public interface IOrderOtpPersistencePort {

    OrderOtp saveOrderOtp(OrderOtp orderOtp);
    OrderOtp findByOrderId(Long orderId);
}
