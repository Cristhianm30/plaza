package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.exception.InvalidFieldsException;
import com.pragma.powerup.domain.exception.InvalidOwnerException;
import com.pragma.powerup.domain.exception.InvalidTokenException;
import com.pragma.powerup.domain.model.Category;
import com.pragma.powerup.domain.model.Dish;
import com.pragma.powerup.domain.model.Restaurant;
import com.pragma.powerup.domain.spi.IDishPersistencePort;
import com.pragma.powerup.domain.spi.IJwtTokenProviderPort;
import com.pragma.powerup.domain.spi.IRestaurantPersistencePort;
import com.pragma.powerup.domain.usecase.validations.DishValidations;
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
public class DishUseCaseTest {

    @Mock
    private IDishPersistencePort dishPersistence;
    @Mock
    private DishValidations dishValidations;
    @Mock
    private IJwtTokenProviderPort jwtTokenProvider;
    @Mock
    private IRestaurantPersistencePort restaurantPersistence;

    @InjectMocks
    private DishUseCase dishUseCase;

    private Dish dish;
    private Restaurant restaurant;
    private Category category;
    private final String validToken = "validToken";
    private final Long ownerId = 100L;

    @BeforeEach
    public void setup() {
        // Se crea una categoría y un restaurante válidos.
        category = new Category(1L, "Entradas", "Descripción de entradas");
        restaurant = new Restaurant(1L, "Restaurante Test", "Calle 123", ownerId, "+123456789", "logo.png", "123456");

        // Se crea un plato con restaurante y categoría asignados.
        dish = new Dish(null, "Ensalada", category, "Ensalada fresca", 15000, restaurant, "image.png", false);
    }

    @Test
    public void testCreateDishSuccessfully() {
        // Configuramos el token y el rol.
        when(jwtTokenProvider.validateToken(validToken)).thenReturn(true);
        when(jwtTokenProvider.getRoleFromToken(validToken)).thenReturn("PROPIETARIO");
        when(jwtTokenProvider.getUserIdFromToken(validToken)).thenReturn(ownerId);
        // Se simula que se encuentra el restaurante con el ownerId esperado.
        when(restaurantPersistence.findById(restaurant.getId())).thenReturn(restaurant);
        // La validación de dish no lanza excepción (doNothing).
        // Se simula que al guardar el dish se asigna un id y se marca como activo.
        Dish savedDish = new Dish(1L, dish.getName(), dish.getCategory(), dish.getDescription(), dish.getPrice(), dish.getRestaurant(), dish.getImageUrl(), true);
        when(dishPersistence.saveDish(any(Dish.class))).thenReturn(savedDish);

        // Ejecutamos el caso de uso.
        Dish result = dishUseCase.createDish(dish, validToken);

        // Verificamos que se invoquen los métodos de las dependencias.
        verify(jwtTokenProvider, times(1)).validateToken(validToken);
        verify(jwtTokenProvider, times(1)).getRoleFromToken(validToken);
        verify(jwtTokenProvider, times(1)).getUserIdFromToken(validToken);
        verify(restaurantPersistence, times(1)).findById(restaurant.getId());
        verify(dishValidations, times(1)).validateDish(dish);
        verify(dishPersistence, times(1)).saveDish(dish);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertTrue(result.getActive());
    }

    @Test
    public void testCreateDishInvalidToken() {
        when(jwtTokenProvider.validateToken(validToken)).thenReturn(false);
        assertThrows(InvalidTokenException.class, () -> dishUseCase.createDish(dish, validToken));
        verify(jwtTokenProvider, times(1)).validateToken(validToken);
        verifyNoMoreInteractions(jwtTokenProvider, restaurantPersistence, dishPersistence, dishValidations);
    }

    @Test
    public void testCreateDishInvalidRole() {
        when(jwtTokenProvider.validateToken(validToken)).thenReturn(true);
        when(jwtTokenProvider.getRoleFromToken(validToken)).thenReturn("CLIENTE");
        assertThrows(InvalidOwnerException.class, () -> dishUseCase.createDish(dish, validToken));
        verify(jwtTokenProvider, times(1)).validateToken(validToken);
        verify(jwtTokenProvider, times(1)).getRoleFromToken(validToken);
    }

    @Test
    public void testCreateDishNullRestaurant() {
        dish.setRestaurant(null);
        when(jwtTokenProvider.validateToken(validToken)).thenReturn(true);
        when(jwtTokenProvider.getRoleFromToken(validToken)).thenReturn("PROPIETARIO");
        when(jwtTokenProvider.getUserIdFromToken(validToken)).thenReturn(ownerId);
        assertThrows(InvalidFieldsException.class, () -> dishUseCase.createDish(dish, validToken));
    }

    @Test
    public void testCreateDishNullCategory() {
        dish.setCategory(null);
        when(jwtTokenProvider.validateToken(validToken)).thenReturn(true);
        when(jwtTokenProvider.getRoleFromToken(validToken)).thenReturn("PROPIETARIO");
        when(jwtTokenProvider.getUserIdFromToken(validToken)).thenReturn(ownerId);
        dish.setRestaurant(restaurant);
        assertThrows(InvalidFieldsException.class, () -> dishUseCase.createDish(dish, validToken));
    }

    @Test
    public void testCreateDishInvalidRestaurantOwner() {
        // Se simula un restaurante cuyo ownerId es diferente al obtenido del token.
        when(jwtTokenProvider.validateToken(validToken)).thenReturn(true);
        when(jwtTokenProvider.getRoleFromToken(validToken)).thenReturn("PROPIETARIO");
        when(jwtTokenProvider.getUserIdFromToken(validToken)).thenReturn(ownerId);
        Restaurant differentOwnerRestaurant = new Restaurant(1L, "Restaurante Test", "Calle 123", 999L, "+123456789", "logo.png", "123456");
        dish.setRestaurant(differentOwnerRestaurant);
        when(restaurantPersistence.findById(differentOwnerRestaurant.getId())).thenReturn(differentOwnerRestaurant);
        assertThrows(InvalidOwnerException.class, () -> dishUseCase.createDish(dish, validToken));
        verify(restaurantPersistence, times(1)).findById(differentOwnerRestaurant.getId());
    }
}