package com.pragma.powerup.domain.spi;


import com.pragma.powerup.domain.model.OrderOtp;

public interface IMessagingFeignPort {
    OrderOtp sendOtp(String phone, Long orderId);
}
