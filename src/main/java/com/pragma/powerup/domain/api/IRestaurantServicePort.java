package com.pragma.powerup.domain.api;

import com.pragma.powerup.domain.model.Restaurant;
import com.pragma.powerup.domain.model.RestaurantPagination;

public interface IRestaurantServicePort {
    Restaurant createRestaurant (Restaurant restaurant);
    RestaurantPagination getAllRestaurantsPaginated(int page, int size, String sortBy);

}
