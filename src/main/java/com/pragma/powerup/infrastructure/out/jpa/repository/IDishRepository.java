package com.pragma.powerup.infrastructure.out.jpa.repository;

import com.pragma.powerup.infrastructure.out.jpa.entity.DishEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface IDishRepository extends JpaRepository<DishEntity, Long> {
    Optional<DishEntity> findById (Long id);

    Page<DishEntity> findAllByRestaurantId(Long restaurantId, Pageable pageable);

    Page<DishEntity> findAllByRestaurantIdAndCategoryId(Long restaurantId, Long categoryId, Pageable pageable);
}
