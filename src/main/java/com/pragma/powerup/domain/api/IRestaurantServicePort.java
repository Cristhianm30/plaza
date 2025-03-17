package com.pragma.powerup.domain.api;

import com.pragma.powerup.application.dto.request.AssignEmployeeRequestDto;
import com.pragma.powerup.domain.model.EmployeeRestaurant;
import com.pragma.powerup.domain.model.Restaurant;
import com.pragma.powerup.domain.model.Pagination;

public interface IRestaurantServicePort {
    Restaurant createRestaurant (Restaurant restaurant);
    Pagination<Restaurant> getAllRestaurantsPaginated(int page, int size, String sortBy);
    EmployeeRestaurant assignEmployeeToRestaurant(Long idRestaurant, AssignEmployeeRequestDto assignEmployeeRequestDto, String token);
}
