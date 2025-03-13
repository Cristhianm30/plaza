package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.api.IDishServicePort;
import com.pragma.powerup.domain.model.Dish;
import com.pragma.powerup.domain.model.Pagination;
import com.pragma.powerup.domain.spi.IDishPersistencePort;
import com.pragma.powerup.domain.spi.IJwtTokenProviderPort;
import com.pragma.powerup.domain.spi.IRestaurantPersistencePort;
import com.pragma.powerup.domain.usecase.validations.DishValidations;
import com.pragma.powerup.domain.usecase.validations.TokenValidations;

public class DishUseCase implements IDishServicePort {

    private final IDishPersistencePort dishPersistence;
    private final DishValidations dishValidations;
    private final TokenValidations tokenValidations;

    public DishUseCase(IDishPersistencePort dishPersistence, DishValidations dishValidations, TokenValidations tokenValidations) {
        this.dishPersistence = dishPersistence;
        this.dishValidations = dishValidations;
        this.tokenValidations = tokenValidations;
    }

    @Override
    public Dish createDish(Dish dish, String token) {
        tokenValidations.validateTokenAndOwnership(token, dish.getRestaurant().getId());
        dish.setActive(true);
        dishValidations.validateDish(dish);
        return dishPersistence.saveDish(dish);
    }

    @Override
    public Dish updateDish(Long id, Dish dish, String token) {

        Dish existingDish = dishPersistence.findById(id);
        tokenValidations.validateTokenAndOwnership(token, existingDish.getRestaurant().getId());

        if (dish.getDescription() != null) {
            existingDish.setDescription(dish.getDescription());
        }
        if (dish.getPrice() != null) {
            existingDish.setPrice(dish.getPrice());
        }

        dishValidations.validateDishUpdate(existingDish);

        return dishPersistence.saveDish(existingDish);
    }

    @Override
    public Dish activeDish(Long id,boolean active, String token) {
        Dish existingDish = dishPersistence.findById(id);
        tokenValidations.validateTokenAndOwnership(token, existingDish.getRestaurant().getId());
        existingDish.setActive(active);
        return dishPersistence.saveDish(existingDish);
    }

    @Override
    public Pagination<Dish> getAllDishesByRestaurant(Long restaurantId, Long categoryId, int page, int size, String sortBy) {
        return dishPersistence.findAllDishesByRestaurant(restaurantId, categoryId, page, size, sortBy);
    }

}
