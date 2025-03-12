package com.pragma.powerup.infrastructure.out.jpa.adapter;

import com.pragma.powerup.domain.exception.RestaurantNotFoundException;
import com.pragma.powerup.domain.model.Category;

import com.pragma.powerup.domain.spi.ICategoryPersistencePort;
import com.pragma.powerup.infrastructure.out.jpa.entity.CategoryEntity;

import com.pragma.powerup.infrastructure.out.jpa.mapper.ICategoryEntityMapper;
import com.pragma.powerup.infrastructure.out.jpa.repository.ICategoryRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CategoryJpaAdapter implements ICategoryPersistencePort {
    private final ICategoryEntityMapper categoryEntityMapper;
    private final ICategoryRepository categoryRepository;

    @Override
    public Category findById (Long id){
        CategoryEntity entity = categoryRepository.findById(id).orElseThrow(RestaurantNotFoundException::new);
        return categoryEntityMapper.entityToModel(entity);

    }
}
