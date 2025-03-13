package com.pragma.powerup.domain.api;

import com.pragma.powerup.domain.model.Dish;
import com.pragma.powerup.domain.model.Pagination;

public interface IDishServicePort {
    Dish createDish(Dish dish, String token);
    Dish updateDish (Long id, Dish dish, String token);
    Dish activeDish (Long id, boolean active, String token);
    Pagination<Dish> getAllDishesByRestaurant (Long id,Long categoryId,int page, int size, String sortBy );
}
