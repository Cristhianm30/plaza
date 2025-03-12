package com.pragma.powerup.domain.spi;

import com.pragma.powerup.domain.model.Restaurant;

public interface IRestaurantPersistencePort {

    Restaurant save (Restaurant restaurant);
    Restaurant findById (Long id);
}
