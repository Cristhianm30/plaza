package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.model.Category;
import com.pragma.powerup.domain.model.Dish;
import com.pragma.powerup.domain.model.Restaurant;
import com.pragma.powerup.domain.spi.ICategoryPersistencePort;
import com.pragma.powerup.domain.spi.IDishPersistencePort;
import com.pragma.powerup.domain.spi.IRestaurantPersistencePort;
import com.pragma.powerup.domain.usecase.validations.DishValidations;
import com.pragma.powerup.domain.usecase.validations.TokenValidations;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
 class DishUseCaseTest {

    @Mock
    private IDishPersistencePort dishPersistence;
    @Mock
    private DishValidations dishValidations;
    @Mock
    private TokenValidations tokenValidations;
    @Mock
    private IRestaurantPersistencePort restaurantPersistencePort;
    @Mock
    private ICategoryPersistencePort categoryPersistencePort;

    @InjectMocks
    private DishUseCase dishUseCase;

    private Dish dish;
    private Restaurant restaurant;
    private Category category;
    private final String token = "validToken";

    @BeforeEach
     void setup() {
        category = new Category(1L, "Entradas", "Descripción de entradas");
        restaurant = new Restaurant(1L, "Restaurante Test", "Calle 123", 100L, "+123456789", "logo.png", "123456");
        dish = new Dish(null, "Ensalada", category, "Ensalada fresca", 15000, restaurant, "image.png", false);
    }

    @Test
     void testCreateDishSuccessfully() {
        when(restaurantPersistencePort.findById(restaurant.getId())).thenReturn(restaurant);
        when(categoryPersistencePort.findById(category.getId())).thenReturn(category);
        when(tokenValidations.cleanedToken(token)).thenReturn("cleanedToken");

        Dish savedDish = new Dish(1L, "Ensalada", category, "Ensalada fresca", 15000, restaurant, "image.png", true);
        when(dishPersistence.saveDish(any(Dish.class))).thenReturn(savedDish);

        Dish result = dishUseCase.createDish(dish, token);

        verify(tokenValidations).validateTokenAndOwnership("cleanedToken", restaurant.getId());
        verify(dishPersistence).saveDish(dish);
        assertEquals(1L, result.getId());
        assertTrue(result.getActive());
    }

    @Test
     void testUpdateDishSuccessfully() {
        Long dishId = 1L;
        Dish existingDish = new Dish(dishId, "Ensalada", category, "Descripción antigua", 15000, restaurant, "image.png", true);
        when(dishPersistence.findById(dishId)).thenReturn(existingDish);
        when(tokenValidations.cleanedToken(token)).thenReturn("cleanedToken");

        Dish updatedDish = new Dish(dishId, "Ensalada", category, "Nueva descripción", 18000, restaurant, "image.png", true);
        when(dishPersistence.saveDish(existingDish)).thenReturn(updatedDish);

        Dish updateData = new Dish(null, null, null, "Nueva descripción", 18000, null, null, null);
        Dish result = dishUseCase.updateDish(dishId, updateData, token);

        verify(tokenValidations).validateTokenAndOwnership("cleanedToken", restaurant.getId());
        assertEquals("Nueva descripción", result.getDescription());
        assertEquals(18000, result.getPrice());
    }

    @Test
     void testActiveDishSuccessfully() {
        Long dishId = 1L;
        Dish existingDish = new Dish(dishId, "Ensalada", category, "Descripción", 15000, restaurant, "image.png", false);
        when(dishPersistence.findById(dishId)).thenReturn(existingDish);
        when(tokenValidations.cleanedToken(token)).thenReturn("cleanedToken");

        Dish activatedDish = new Dish(dishId, "Ensalada", category, "Descripción", 15000, restaurant, "image.png", true);
        when(dishPersistence.saveDish(existingDish)).thenReturn(activatedDish);

        Dish result = dishUseCase.activeDish(dishId, true, token);

        verify(tokenValidations).validateTokenAndOwnership("cleanedToken", restaurant.getId());
        assertTrue(result.getActive());
    }
}
