package com.pragma.powerup.infrastructure.out.jpa.adapter;

import com.pragma.powerup.domain.model.EmployeeRestaurant;
import com.pragma.powerup.domain.spi.IEmployeeRestaurantPersistencePort;
import com.pragma.powerup.infrastructure.out.jpa.entity.EmployeeRestaurantEntity;
import com.pragma.powerup.infrastructure.out.jpa.mapper.IEmployeeRestaurantEntityMapper;
import com.pragma.powerup.infrastructure.out.jpa.repository.IEmployeeRestaurantRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EmployeeRestaurantJpaAdapter implements IEmployeeRestaurantPersistencePort {

    private final IEmployeeRestaurantRepository employeeRestaurantRepository;
    private final IEmployeeRestaurantEntityMapper employeeRestaurantEntityMapper;

    @Override
    public EmployeeRestaurant saveEmployee(EmployeeRestaurant employeeRestaurant) {
        EmployeeRestaurantEntity entity = employeeRestaurantEntityMapper.modelToEntity(employeeRestaurant);
        EmployeeRestaurantEntity savedEntity = employeeRestaurantRepository.save(entity);
        return employeeRestaurantEntityMapper.entityToModel(savedEntity);
    }
}
