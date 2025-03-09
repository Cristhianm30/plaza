package com.pragma.powerup.domain.model;

import java.time.LocalDate;

public class Order {
    private Long id;
    private Long clientId;
    private LocalDate date;
    private String status;
    private Long chefId;
    private Restaurant restaurant;

    public Order(LocalDate date, Long id, Long clientId, String status, Long chefId, Restaurant restaurant) {
        this.date = date;
        this.id = id;
        this.clientId = clientId;
        this.status = status;
        this.chefId = chefId;
        this.restaurant = restaurant;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getChefId() {
        return chefId;
    }

    public void setChefId(Long chefId) {
        this.chefId = chefId;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }
}
