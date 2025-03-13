package com.pragma.powerup.domain.spi;

import com.pragma.powerup.domain.model.Dish;
import com.pragma.powerup.domain.model.Pagination;

public interface IDishPersistencePort {
    Dish saveDish(Dish dish);
    Dish findById(Long id);
    Pagination<Dish> findAllDishesByRestaurant(Long restaurantId, Long categoryId, int page, int size, String sortBy);
}
