package com.pragma.powerup.domain.model;

import java.time.LocalDateTime;

public class OrderOtp {
    private Long id;
    private Long orderId;
    private Long clientId;
    private String otp;
    private String status;
    private LocalDateTime createdAt;

    public OrderOtp() {
    }

    public OrderOtp(Long orderId, Long clientId, String otp, String status, LocalDateTime createdAt) {
        this.orderId = orderId;
        this.clientId = clientId;
        this.otp = otp;
        this.status = status;
        this.createdAt = createdAt;
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

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}