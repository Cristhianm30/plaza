package com.pragma.powerup.domain.api;

import com.pragma.powerup.domain.model.Restaurant;
import com.pragma.powerup.domain.model.Pagination;

public interface IRestaurantServicePort {
    Restaurant createRestaurant (Restaurant restaurant);
    Pagination<Restaurant> getAllRestaurantsPaginated(int page, int size, String sortBy);

}
