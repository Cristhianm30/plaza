package com.pragma.powerup.application.handler.impl;

import com.pragma.powerup.application.dto.request.RestaurantRequestDto;
import com.pragma.powerup.application.dto.response.RestaurantPaginationResponseDto;
import com.pragma.powerup.application.dto.response.RestaurantItemResponseDto;
import com.pragma.powerup.application.dto.response.RestaurantResponseDto;
import com.pragma.powerup.application.handler.IRestaurantHandler;
import com.pragma.powerup.application.mapper.IRestaurantRequestMapper;
import com.pragma.powerup.application.mapper.IRestaurantResponseMapper;
import com.pragma.powerup.domain.api.IRestaurantServicePort;
import com.pragma.powerup.domain.model.Restaurant;
import com.pragma.powerup.domain.model.RestaurantPagination;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RestaurantHandlerImpl implements IRestaurantHandler {

    private final IRestaurantServicePort restaurantServicePort;
    private final IRestaurantRequestMapper restaurantRequestMapper;
    private final IRestaurantResponseMapper restaurantResponseMapper;


    @Override
    public RestaurantResponseDto createRestaurant(RestaurantRequestDto restaurantRequestDto) {
        Restaurant request = restaurantRequestMapper.requestToModel(restaurantRequestDto);
        Restaurant response = restaurantServicePort.createRestaurant(request);
        return restaurantResponseMapper.modelToResponse(response);
    }

    @Override
    public RestaurantPaginationResponseDto<RestaurantItemResponseDto> getPaginatedRestaurants(int page, int size, String sortBy) {
        RestaurantPagination<Restaurant> pagination = restaurantServicePort.getAllRestaurantsPaginated(page, size, sortBy);

        return RestaurantPaginationResponseDto.<RestaurantItemResponseDto>builder()
                .restaurants(
                        pagination.getRestaurants().stream()
                                .map(restaurant -> restaurantResponseMapper.toRestaurantItemDto(restaurant))
                                .collect(Collectors.toList())
                )
                .currentPage(pagination.getCurrentPage())
                .totalPages(pagination.getTotalPages())
                .totalItems(pagination.getTotalItems())
                .build();
    }
}

