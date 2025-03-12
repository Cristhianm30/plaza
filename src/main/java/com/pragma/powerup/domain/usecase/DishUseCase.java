package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.api.IDishServicePort;
import com.pragma.powerup.domain.exception.InvalidFieldsException;
import com.pragma.powerup.domain.exception.InvalidOwnerException;
import com.pragma.powerup.domain.exception.InvalidTokenException;
import com.pragma.powerup.domain.model.Dish;
import com.pragma.powerup.domain.model.Restaurant;
import com.pragma.powerup.domain.spi.IDishPersistencePort;
import com.pragma.powerup.domain.spi.IJwtTokenProviderPort;
import com.pragma.powerup.domain.spi.IRestaurantPersistencePort;
import com.pragma.powerup.domain.usecase.validations.DishValidations;

public class DishUseCase implements IDishServicePort {

    private final IDishPersistencePort dishPersistence;
    private final DishValidations dishValidations;
    private final IJwtTokenProviderPort jwtTokenProvider;
    private final IRestaurantPersistencePort restaurantPersistence;

    public DishUseCase(IDishPersistencePort dishPersistence, DishValidations dishValidations, IJwtTokenProviderPort jwtTokenProvider, IRestaurantPersistencePort restaurantPersistence) {
        this.dishPersistence = dishPersistence;
        this.dishValidations = dishValidations;
        this.jwtTokenProvider = jwtTokenProvider;
        this.restaurantPersistence = restaurantPersistence;
    }

    @Override
    public Dish createDish(Dish dish, String token) {
        // Validar token
        if (!jwtTokenProvider.validateToken(token)) {
            throw new InvalidTokenException();
        }

        // Extraer datos del token
        String role = jwtTokenProvider.getRoleFromToken(token);
        Long ownerId = jwtTokenProvider.getUserIdFromToken(token);

        // Validar rol
        if (!"PROPIETARIO".equals(role)) {
            throw new InvalidOwnerException();
        }

        // Validar que el restaurante y la categoría no sean nulos
        if (dish.getRestaurant() == null || dish.getRestaurant().getId() == null) {
            throw new InvalidFieldsException();
        }
        if (dish.getCategory() == null || dish.getCategory().getId() == null) {
            throw new InvalidFieldsException();
        }

        // Validar propiedad del restaurante
        Restaurant restaurant = restaurantPersistence.findById(dish.getRestaurant().getId());
        if (!restaurant.getOwnerId().equals(ownerId)) {
            throw new InvalidOwnerException();
        }

        // Validaciones de dominio
        dish.setActive(true);
        dishValidations.validateDish(dish);

        // Guardar
        return dishPersistence.saveDish(dish);
    }

}
