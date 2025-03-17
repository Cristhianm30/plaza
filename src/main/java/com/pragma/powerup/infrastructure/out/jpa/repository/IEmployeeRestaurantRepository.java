package com.pragma.powerup.infrastructure.out.jpa.repository;

import com.pragma.powerup.infrastructure.out.jpa.entity.EmployeeRestaurantEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IEmployeeRestaurantRepository extends JpaRepository<EmployeeRestaurantEntity, Long> {
    Optional<EmployeeRestaurantEntity> findByEmployeeId (Long id);
}
