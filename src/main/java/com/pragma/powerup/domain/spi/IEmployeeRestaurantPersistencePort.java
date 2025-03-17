package com.pragma.powerup.domain.spi;

import com.pragma.powerup.domain.model.EmployeeRestaurant;

public interface IEmployeeRestaurantPersistencePort {
    EmployeeRestaurant saveEmployee(EmployeeRestaurant employeeRestaurant);
}
