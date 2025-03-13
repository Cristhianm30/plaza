package com.pragma.powerup.application.handler.impl;

import com.pragma.powerup.application.dto.request.DishActiveRequestDto;
import com.pragma.powerup.application.dto.request.DishRequestDto;
import com.pragma.powerup.application.dto.request.DishUpdateRequestDto;
import com.pragma.powerup.application.dto.response.DishResponseDto;
import com.pragma.powerup.application.handler.IDishHandler;
import com.pragma.powerup.application.mapper.IDishRequestMapper;
import com.pragma.powerup.application.mapper.IDishResponseMapper;
import com.pragma.powerup.domain.api.IDishServicePort;
import com.pragma.powerup.domain.exception.CategoryNotFoundException;
import com.pragma.powerup.domain.exception.RestaurantNotFoundException;
import com.pragma.powerup.domain.model.Category;
import com.pragma.powerup.domain.model.Dish;
import com.pragma.powerup.domain.model.Restaurant;
import com.pragma.powerup.domain.spi.ICategoryPersistencePort;
import com.pragma.powerup.domain.spi.IDishPersistencePort;
import com.pragma.powerup.domain.spi.IRestaurantPersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DishHandlerImpl implements IDishHandler {

    private final IDishRequestMapper dishRequestMapper;
    private final IDishResponseMapper dishResponseMapper;
    private final IDishServicePort dishServicePort;
    private final IRestaurantPersistencePort restaurantPersistence;
    private final ICategoryPersistencePort categoryPersistence;
    private final IDishPersistencePort persistencePort;

    @Override
    public DishResponseDto createDish(DishRequestDto dishRequest, String token) {

        String cleanedToken = token.replace("Bearer ", "");

        // Mapear DTO a modelo
        Dish dish = dishRequestMapper.requestToModel(dishRequest);

        // Obtener el restaurante y asignarlo
        Restaurant restaurant = restaurantPersistence.findById(dishRequest.getRestaurantId());
        if (restaurant == null) throw new RestaurantNotFoundException();
        dish.setRestaurant(restaurant);

        // Obtener la categoría y asignarla
        Category category = categoryPersistence.findById(dishRequest.getCategoryId());
        if (category == null) throw new CategoryNotFoundException();
        dish.setCategory(category);


        Dish savedDish = dishServicePort.createDish(dish,cleanedToken);
        return dishResponseMapper.modelToResponse(savedDish);
    }


    @Override
    public DishResponseDto updateDish(Long id, DishUpdateRequestDto dishUpdateRequest, String token) {

        String cleanedToken = token.replace("Bearer ", "");

        Dish update = dishRequestMapper.updaterequestToModel(dishUpdateRequest);
        Dish updated = dishServicePort.updateDish(id,update,cleanedToken);
        return dishResponseMapper.modelToResponse(updated);
    }

    @Override
    public DishResponseDto activeDish(Long id, DishActiveRequestDto dishActiveRequestDto, String token){

        String cleanedToken = token.replace("Bearer ", "");

        Dish activeDish = dishServicePort.activeDish(id, dishActiveRequestDto.isActive(),cleanedToken);
        return dishResponseMapper.modelToResponse(activeDish);
    }

}
