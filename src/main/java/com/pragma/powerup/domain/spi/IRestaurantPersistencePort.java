package com.pragma.powerup.domain.spi;

import com.pragma.powerup.domain.model.Restaurant;
import com.pragma.powerup.domain.model.RestaurantPagination;

public interface IRestaurantPersistencePort {

    Restaurant save (Restaurant restaurant);
    Restaurant findById (Long id);
    RestaurantPagination findAllPaginated(int page, int size, String sortBy);
}
