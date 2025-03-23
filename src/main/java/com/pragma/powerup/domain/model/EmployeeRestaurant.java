package com.pragma.powerup.domain.model;

public class EmployeeRestaurant {
    private Long id;
    private Long employeeId;
    private Long restaurantId;

    public EmployeeRestaurant(Long id, Long employeeId, Long restaurantId) {
        this.id = id;
        this.employeeId = employeeId;
        this.restaurantId = restaurantId;
    }

    public EmployeeRestaurant() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public Long getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Long restaurantId) {
        this.restaurantId = restaurantId;
    }
}
