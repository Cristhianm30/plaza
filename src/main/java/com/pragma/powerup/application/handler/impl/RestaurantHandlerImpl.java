package com.pragma.powerup.application.handler.impl;

import com.pragma.powerup.application.dto.request.AssignEmployeeRequestDto;
import com.pragma.powerup.application.dto.request.RestaurantRequestDto;
import com.pragma.powerup.application.dto.response.AssignEmployeeResponseDto;
import com.pragma.powerup.application.dto.response.PaginationResponseDto;
import com.pragma.powerup.application.dto.response.RestaurantItemResponseDto;
import com.pragma.powerup.application.dto.response.RestaurantResponseDto;
import com.pragma.powerup.application.handler.IRestaurantHandler;
import com.pragma.powerup.application.mapper.IEmployeeRestaurantResponseMapper;
import com.pragma.powerup.application.mapper.IRestaurantRequestMapper;
import com.pragma.powerup.application.mapper.IRestaurantResponseMapper;
import com.pragma.powerup.domain.api.IRestaurantServicePort;
import com.pragma.powerup.domain.model.EmployeeRestaurant;
import com.pragma.powerup.domain.model.Restaurant;
import com.pragma.powerup.domain.model.Pagination;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RestaurantHandlerImpl implements IRestaurantHandler {

    private final IRestaurantServicePort restaurantServicePort;
    private final IRestaurantRequestMapper restaurantRequestMapper;
    private final IRestaurantResponseMapper restaurantResponseMapper;
    private final IEmployeeRestaurantResponseMapper employeeRestaurantResponseMapper;


    @Override
    public RestaurantResponseDto createRestaurant(RestaurantRequestDto restaurantRequestDto) {
        Restaurant request = restaurantRequestMapper.requestToModel(restaurantRequestDto);
        Restaurant response = restaurantServicePort.createRestaurant(request);
        return restaurantResponseMapper.modelToResponse(response);
    }

    @Override
    public PaginationResponseDto<RestaurantItemResponseDto> getPaginatedRestaurants(int page, int size, String sortBy) {
        Pagination<Restaurant> pagination = restaurantServicePort.getAllRestaurantsPaginated(page, size, sortBy);

        return PaginationResponseDto.<RestaurantItemResponseDto>builder()
                .items(
                        pagination.getItems().stream()
                                .map(restaurantResponseMapper::modelToItemDto)
                                .collect(Collectors.toList())
                )
                .currentPage(pagination.getCurrentPage())
                .totalPages(pagination.getTotalPages())
                .totalItems(pagination.getTotalItems())
                .build();
    }

    @Override
    public AssignEmployeeResponseDto assignEmployeeToRestaurant(Long idRestaurant, AssignEmployeeRequestDto assignEmployeeRequestDto, String token) {
        EmployeeRestaurant employeeRestaurant = restaurantServicePort.assignEmployeeToRestaurant(idRestaurant, assignEmployeeRequestDto, token);
        return employeeRestaurantResponseMapper.modelToResponse(employeeRestaurant);
    }


}

