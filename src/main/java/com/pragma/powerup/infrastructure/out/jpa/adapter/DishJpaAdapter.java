package com.pragma.powerup.infrastructure.out.jpa.adapter;

import com.pragma.powerup.domain.model.Dish;
import com.pragma.powerup.domain.spi.IDishPersistencePort;
import com.pragma.powerup.infrastructure.out.jpa.entity.DishEntity;
import com.pragma.powerup.infrastructure.out.jpa.mapper.IDishEntityMapper;
import com.pragma.powerup.infrastructure.out.jpa.repository.IDishRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DishJpaAdapter implements IDishPersistencePort {

    private final IDishEntityMapper dishEntityMapper;
    private final IDishRepository dishRepository;


    @Override
    public Dish saveDish(Dish dish) {
        DishEntity entity = dishEntityMapper.modelToEntity(dish);
        DishEntity savedEntity = dishRepository.save(entity);
        return dishEntityMapper.entityToModel(savedEntity);
    }
}
