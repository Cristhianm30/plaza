package com.pragma.powerup.infrastructure.out.jpa.adapter;

import com.pragma.powerup.domain.exception.RestaurantNotFoundException;
import com.pragma.powerup.domain.model.Restaurant;
import com.pragma.powerup.domain.spi.IRestaurantPersistencePort;
import com.pragma.powerup.domain.model.Pagination;
import com.pragma.powerup.infrastructure.out.jpa.entity.RestaurantEntity;
import com.pragma.powerup.infrastructure.out.jpa.mapper.IRestaurantEntityMapper;
import com.pragma.powerup.infrastructure.out.jpa.repository.IRestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.stream.Collectors;

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

    @Override
    public Pagination<Restaurant> findAllPaginated(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, sortBy));
        Page<RestaurantEntity> entityPage = restaurantRepository.findAll(pageable);

        List<Restaurant> restaurants = entityPage.getContent()
                .stream()
                .map(restaurantEntityMapper::entityToModel)
                .collect(Collectors.toList());

        return new Pagination<Restaurant>(
                restaurants,
                entityPage.getNumber(),
                entityPage.getTotalPages(),
                entityPage.getTotalElements()
        );
    }

    @Override
    public Restaurant findRestaurantByOwnerId(Long ownerId) {

        RestaurantEntity entity = restaurantRepository.findRestaurantByOwnerId(ownerId).orElseThrow(RestaurantNotFoundException::new);

        return restaurantEntityMapper.entityToModel(entity);
    }
}
