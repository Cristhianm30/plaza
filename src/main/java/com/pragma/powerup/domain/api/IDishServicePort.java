package com.pragma.powerup.domain.api;

import com.pragma.powerup.domain.model.Dish;

public interface IDishServicePort {
    Dish createDish(Dish dish, String token);
    Dish updateDish (Long id, Dish dish, String token);
    Dish activeDish (Long id, boolean active, String token);
}
