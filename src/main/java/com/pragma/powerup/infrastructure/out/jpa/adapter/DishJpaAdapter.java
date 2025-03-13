package com.pragma.powerup.infrastructure.out.jpa.adapter;

import com.pragma.powerup.domain.exception.DishNotFoundException;
import com.pragma.powerup.domain.model.Dish;
import com.pragma.powerup.domain.model.Pagination;
import com.pragma.powerup.domain.spi.IDishPersistencePort;
import com.pragma.powerup.infrastructure.out.jpa.entity.DishEntity;
import com.pragma.powerup.infrastructure.out.jpa.mapper.IDishEntityMapper;
import com.pragma.powerup.infrastructure.out.jpa.repository.IDishRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.stream.Collectors;

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

    @Override
    public Dish findById (Long id){

        DishEntity entity = dishRepository.findById(id).orElseThrow(DishNotFoundException::new);
        return dishEntityMapper.entityToModel(entity);
    }

    @Override
    public Pagination<Dish> findAllDishesByRestaurant(Long restaurantId, Long categoryId, int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, sortBy));
        Page<DishEntity> entityPage;

        if (categoryId == null) {
            entityPage = dishRepository.findAllByRestaurantId(restaurantId, pageable);
        } else {
            entityPage = dishRepository.findAllByRestaurantIdAndCategoryId(restaurantId, categoryId, pageable);
        }

        List<Dish> dishes = entityPage.getContent().stream()
                .map(dishEntityMapper::entityToModel)
                .collect(Collectors.toList());

        return new Pagination<>(
                dishes,
                entityPage.getNumber(),
                entityPage.getTotalPages(),
                entityPage.getTotalElements()
        );
    }

}
