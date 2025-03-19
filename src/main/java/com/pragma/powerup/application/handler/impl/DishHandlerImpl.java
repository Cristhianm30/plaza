package com.pragma.powerup.application.handler.impl;

import com.pragma.powerup.application.dto.request.DishActiveRequestDto;
import com.pragma.powerup.application.dto.request.DishRequestDto;
import com.pragma.powerup.application.dto.request.DishUpdateRequestDto;
import com.pragma.powerup.application.dto.response.DishResponseDto;
import com.pragma.powerup.application.dto.response.PaginationResponseDto;
import com.pragma.powerup.application.handler.IDishHandler;
import com.pragma.powerup.application.mapper.IDishRequestMapper;
import com.pragma.powerup.application.mapper.IDishResponseMapper;
import com.pragma.powerup.domain.api.IDishServicePort;
import com.pragma.powerup.domain.model.Category;
import com.pragma.powerup.domain.model.Dish;
import com.pragma.powerup.domain.model.Pagination;

import com.pragma.powerup.domain.model.Restaurant;
import com.pragma.powerup.domain.spi.ICategoryPersistencePort;
import com.pragma.powerup.domain.spi.IRestaurantPersistencePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DishHandlerImpl implements IDishHandler {

    private final IDishRequestMapper dishRequestMapper;
    private final IDishResponseMapper dishResponseMapper;
    private final IDishServicePort dishServicePort;
    private final IRestaurantPersistencePort restaurantPersistencePort;
    private final ICategoryPersistencePort categoryPersistencePort;



    @Override
    public DishResponseDto createDish(DishRequestDto dishRequest, String token) {

        Restaurant restaurant = restaurantPersistencePort.findById(dishRequest.getRestaurantId());
        Category category = categoryPersistencePort.findById(dishRequest.getCategoryId());

        Dish dish = dishRequestMapper.requestToModel(dishRequest);
        // aqui podría sacar del request las Id y enviarlas como Long en el service port y evitar las persistencias en este handler
        dish.setCategory(category);
        dish.setRestaurant(restaurant);

        Dish savedDish = dishServicePort.createDish(dish,token);

        return dishResponseMapper.modelToResponse(savedDish);
    }


    @Override
    public DishResponseDto updateDish(Long id, DishUpdateRequestDto dishUpdateRequest, String token) {

        Dish update = dishRequestMapper.updateRequestToModel(dishUpdateRequest);
        Dish updated = dishServicePort.updateDish(id,update,token);

        return dishResponseMapper.modelToResponse(updated);
    }

    @Override
    public DishResponseDto activeDish(Long id, DishActiveRequestDto dishActiveRequestDto, String token){

        Dish activeDish = dishServicePort.activeDish(id, dishActiveRequestDto.isActive(),token);

        return dishResponseMapper.modelToResponse(activeDish);
    }

    @Override
    public PaginationResponseDto<DishResponseDto> getAllDishesByRestaurant(Long restaurantId, Long categoryId, int page, int size, String sortBy) {

        Pagination<Dish> pagination = dishServicePort.getAllDishesByRestaurant(restaurantId, categoryId, page, size, sortBy);

        return PaginationResponseDto.<DishResponseDto>builder()
                .items(
                        pagination.getItems().stream()
                                .map(dishResponseMapper::modelToResponse)
                                .collect(Collectors.toList())
                )
                .currentPage(pagination.getCurrentPage())
                .totalPages(pagination.getTotalPages())
                .totalItems(pagination.getTotalItems())
                .build();
    }

}
