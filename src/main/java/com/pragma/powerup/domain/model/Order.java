package com.pragma.powerup.domain.model;


import java.time.LocalDateTime;
import java.util.List;

public class Order {
    private Long id;
    private Long clientId;
    private Long chefId;
    private Restaurant restaurant;
    private String status;
    private LocalDateTime date;
    private List<OrderDish> dishes;

    public Order(Long id, Long clientId,Long chefId, Restaurant restaurant, String status, LocalDateTime date, List<OrderDish> dishes) {
        this.id = id;
        this.clientId = clientId;
        this.chefId = chefId;
        this.restaurant = restaurant;
        this.status = status;
        this.date = date;
        this.dishes = dishes;
    }

    public Long getChefId() {return chefId;}

    public void setChefId(Long chefId) {this.chefId = chefId;}

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

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public List<OrderDish> getDishes() {
        return dishes;
    }

    public void setDishes(List<OrderDish> dishes) {
        this.dishes = dishes;
    }
}

