package com.pragma.powerup.domain.spi;

import com.pragma.powerup.domain.model.Restaurant;
import com.pragma.powerup.domain.model.Pagination;

public interface IRestaurantPersistencePort {

    Restaurant save (Restaurant restaurant);
    Restaurant findById (Long id);
    Pagination<Restaurant> findAllPaginated(int page, int size, String sortBy);
}
