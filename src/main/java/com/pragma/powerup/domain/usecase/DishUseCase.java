package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.api.IDishServicePort;
import com.pragma.powerup.domain.exception.CategoryNotFoundException;
import com.pragma.powerup.domain.exception.RestaurantNotFoundException;
import com.pragma.powerup.domain.model.Category;
import com.pragma.powerup.domain.model.Dish;
import com.pragma.powerup.domain.model.Pagination;
import com.pragma.powerup.domain.model.Restaurant;
import com.pragma.powerup.domain.spi.ICategoryPersistencePort;
import com.pragma.powerup.domain.spi.IDishPersistencePort;
import com.pragma.powerup.domain.spi.IRestaurantPersistencePort;
import com.pragma.powerup.domain.usecase.validations.DishValidations;
import com.pragma.powerup.domain.usecase.validations.TokenValidations;

public class DishUseCase implements IDishServicePort {

    private final IDishPersistencePort dishPersistence;
    private final DishValidations dishValidations;
    private final TokenValidations tokenValidations;
    private final IRestaurantPersistencePort restaurantPersistencePort;
    private final ICategoryPersistencePort categoryPersistencePort;

    public DishUseCase(IDishPersistencePort dishPersistence, DishValidations dishValidations, TokenValidations tokenValidations, IRestaurantPersistencePort restaurantPersistencePort, ICategoryPersistencePort categoryPersistencePort) {
        this.dishPersistence = dishPersistence;
        this.dishValidations = dishValidations;
        this.tokenValidations = tokenValidations;
        this.restaurantPersistencePort = restaurantPersistencePort;
        this.categoryPersistencePort = categoryPersistencePort;
    }

    @Override
    public Dish createDish(Dish dish, String token) {

        String cleanedToken = tokenValidations.cleanedToken(token);
        tokenValidations.validateTokenAndOwnership(cleanedToken, dish.getRestaurant().getId());

        Restaurant restaurant = restaurantPersistencePort.findById(dish.getRestaurant().getId());
        if (restaurant == null) {
            throw new RestaurantNotFoundException();
        }

        Category category = categoryPersistencePort.findById(dish.getCategory().getId());
        if (category == null) {
            throw new CategoryNotFoundException();
        }

        dish.setRestaurant(restaurant);
        dish.setCategory(category);
        dish.setActive(true);
        dishValidations.validateDish(dish);

        return dishPersistence.saveDish(dish);
    }

    @Override
    public Dish updateDish(Long id, Dish dish, String token) {

        String cleanedToken = tokenValidations.cleanedToken(token);

        Dish existingDish = dishPersistence.findById(id);
        tokenValidations.validateTokenAndOwnership(cleanedToken, existingDish.getRestaurant().getId());

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

        String cleanedToken = tokenValidations.cleanedToken(token);
        Dish existingDish = dishPersistence.findById(id);
        tokenValidations.validateTokenAndOwnership(cleanedToken, existingDish.getRestaurant().getId());
        existingDish.setActive(active);

        return dishPersistence.saveDish(existingDish);
    }

    @Override
    public Pagination<Dish> getAllDishesByRestaurant(Long restaurantId, Long categoryId, int page, int size, String sortBy) {
        return dishPersistence.findAllDishesByRestaurant(restaurantId, categoryId, page, size, sortBy);
    }

}
