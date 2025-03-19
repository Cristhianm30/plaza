package com.pragma.powerup.domain.model;

import java.time.LocalDateTime;

public class OrderOtp {

    private Long id;
    private Long orderId;
    private String otp;

    public OrderOtp() {
    }

    public OrderOtp(Long id, Long orderId, String otp) {
        this.id = id;
        this.orderId = orderId;
        this.otp = otp;
    }

    public OrderOtp(Long orderId, String otp) {
        this.orderId = orderId;
        this.otp = otp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }


}