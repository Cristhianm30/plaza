package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.api.IRestaurantServicePort;
import com.pragma.powerup.domain.exception.InvalidOwnerException;
import com.pragma.powerup.domain.model.Pagination;
import com.pragma.powerup.domain.model.Restaurant;
import com.pragma.powerup.domain.spi.IRestaurantPersistencePort;
import com.pragma.powerup.domain.spi.IUserFeignPort;
import com.pragma.powerup.domain.usecase.validations.RestaurantValidations;

public class RestaurantUseCase  implements IRestaurantServicePort {
    private final IRestaurantPersistencePort restaurantPersistencePort;
    private final RestaurantValidations restaurantValidations;
    private final IUserFeignPort userFeignPort;


    public RestaurantUseCase( IRestaurantPersistencePort restaurantPersistencePort, RestaurantValidations restaurantValidations, IUserFeignPort userFeignPort) {
        this.restaurantPersistencePort = restaurantPersistencePort;
        this.restaurantValidations = restaurantValidations;
        this.userFeignPort = userFeignPort;
    }

    @Override
    public Restaurant createRestaurant(Restaurant restaurant){
        restaurantValidations.validateRestaurant(restaurant);
        String userRole = userFeignPort.getUserRole(restaurant.getOwnerId());
        restaurantValidations.validateOwnerRole(userRole);
        return restaurantPersistencePort.save(restaurant);

    }

    @Override
    public Pagination<Restaurant> getAllRestaurantsPaginated(int page, int size, String sortBy) {
        return restaurantPersistencePort.findAllPaginated(page, size, sortBy);
    }



}
