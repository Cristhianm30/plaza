package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.model.Category;
import com.pragma.powerup.domain.model.Dish;
import com.pragma.powerup.domain.model.Restaurant;
import com.pragma.powerup.domain.spi.IDishPersistencePort;
import com.pragma.powerup.domain.usecase.validations.DishValidations;
import com.pragma.powerup.domain.usecase.validations.TokenValidations;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DishUseCaseTest {

    @Mock
    private IDishPersistencePort dishPersistence;
    @Mock
    private DishValidations dishValidations;
    @Mock
    private TokenValidations tokenValidations;

    @InjectMocks
    private DishUseCase dishUseCase;

    private Dish dish;
    private Restaurant restaurant;
    private Category category;
    private final String token = "validToken";

    @BeforeEach
    public void setup() {
        // Se crea una categoría y un restaurante válidos
        category = new Category(1L, "Entradas", "Descripción de entradas");
        restaurant = new Restaurant(1L, "Restaurante Test", "Calle 123", 100L, "+123456789", "logo.png", "123456");
        // Se crea un plato con restaurante y categoría asignados
        dish = new Dish(null, "Ensalada", category, "Ensalada fresca", 15000, restaurant, "image.png", false);
    }

    @Test
    public void testCreateDishSuccessfully() {
        // Suponemos que tokenValidations.validateTokenAndOwnership no lanza excepción
        // Se simula que al guardar el plato se asigna un id y se marca como activo.
        Dish savedDish = new Dish(1L, dish.getName(), dish.getCategory(), dish.getDescription(), dish.getPrice(),
                dish.getRestaurant(), dish.getImageUrl(), true);
        when(dishPersistence.saveDish(any(Dish.class))).thenReturn(savedDish);

        Dish result = dishUseCase.createDish(dish, token);

        verify(tokenValidations, times(1)).validateTokenAndOwnership(token, restaurant.getId());
        verify(dishValidations, times(1)).validateDish(dish);
        verify(dishPersistence, times(1)).saveDish(dish);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertTrue(result.getActive());
    }

    @Test
    public void testUpdateDishSuccessfully() {
        Long dishId = 1L;
        // Plato existente simulado
        Dish existingDish = new Dish(dishId, "Ensalada", dish.getCategory(), "Descripción antigua", 15000,
                dish.getRestaurant(), dish.getImageUrl(), true);
        when(dishPersistence.findById(dishId)).thenReturn(existingDish);
        // Objeto con los campos a actualizar (por ejemplo, descripción y precio)
        Dish updateDish = new Dish(null, null, null, "Nueva descripción", 18000, null, null, null);
        // Simulamos que al guardar se retorna el plato actualizado
        Dish updatedDish = new Dish(dishId, "Ensalada", dish.getCategory(), "Nueva descripción", 18000,
                dish.getRestaurant(), dish.getImageUrl(), true);
        when(dishPersistence.saveDish(existingDish)).thenReturn(updatedDish);

        Dish result = dishUseCase.updateDish(dishId, updateDish, token);

        verify(tokenValidations, times(1)).validateTokenAndOwnership(token, existingDish.getRestaurant().getId());
        verify(dishValidations, times(1)).validateDishUpdate(existingDish);
        verify(dishPersistence, times(1)).saveDish(existingDish);

        assertNotNull(result);
        assertEquals("Nueva descripción", result.getDescription());
        assertEquals(18000, result.getPrice());
    }

    @Test
    public void testActiveDishSuccessfully() {
        Long dishId = 1L;
        // Plato existente simulado con estado activo false
        Dish existingDish = new Dish(dishId, "Ensalada", dish.getCategory(), "Descripción", 15000,
                dish.getRestaurant(), dish.getImageUrl(), false);
        when(dishPersistence.findById(dishId)).thenReturn(existingDish);
        // Simulamos que al guardar se retorna el plato con el estado actualizado
        Dish activatedDish = new Dish(dishId, "Ensalada", dish.getCategory(), "Descripción", 15000,
                dish.getRestaurant(), dish.getImageUrl(), true);
        when(dishPersistence.saveDish(existingDish)).thenReturn(activatedDish);

        Dish result = dishUseCase.activeDish(dishId, true, token);

        verify(tokenValidations, times(1)).validateTokenAndOwnership(token, existingDish.getRestaurant().getId());
        verify(dishPersistence, times(1)).saveDish(existingDish);
        assertNotNull(result);
        assertTrue(result.getActive());
    }
}
