package com.pragma.powerup.infrastructure.out.jpa.adapter;

import com.pragma.powerup.domain.exception.RestaurantNotFoundException;
import com.pragma.powerup.domain.model.Restaurant;
import com.pragma.powerup.domain.spi.IRestaurantPersistencePort;
import com.pragma.powerup.infrastructure.out.jpa.entity.RestaurantEntity;
import com.pragma.powerup.infrastructure.out.jpa.mapper.IRestaurantEntityMapper;
import com.pragma.powerup.infrastructure.out.jpa.repository.IRestaurantRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RestaurantJpaAdapter implements IRestaurantPersistencePort {

    private final IRestaurantEntityMapper restaurantEntityMapper;
    private final IRestaurantRepository restaurantRepository;


    @Override
    public Restaurant save(Restaurant restaurant) {
        RestaurantEntity entity = restaurantEntityMapper.modelToEntity(restaurant);
        RestaurantEntity savedEntity = restaurantRepository.save(entity);
        return restaurantEntityMapper.entityToModel(savedEntity);

    }

    @Override
    public Restaurant findById (Long id){
            RestaurantEntity entity = restaurantRepository.findById(id).orElseThrow(RestaurantNotFoundException::new);
            return restaurantEntityMapper.entityToModel(entity);

    }
}
