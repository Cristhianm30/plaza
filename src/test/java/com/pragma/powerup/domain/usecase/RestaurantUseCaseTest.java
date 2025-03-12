package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.exception.InvalidOwnerException;
import com.pragma.powerup.domain.model.Restaurant;
import com.pragma.powerup.domain.spi.IRestaurantPersistencePort;
import com.pragma.powerup.domain.spi.IUserFeignPort;
import com.pragma.powerup.domain.usecase.validations.RestaurantValidations;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RestaurantUseCaseTest {

    @Mock
    private IRestaurantPersistencePort restaurantPersistencePort;
    @Mock
    private RestaurantValidations restaurantValidations;
    @Mock
    private IUserFeignPort userFeignPort;

    @InjectMocks
    private RestaurantUseCase restaurantUseCase;

    private Restaurant restaurant;

    @BeforeEach
    public void setup() {
        // Se crea un restaurante válido.
        restaurant = new Restaurant(null, "Restaurante Test", "Calle 123", 100L, "+123456789", "logo.png", "123456");
    }

    @Test
    public void testCreateRestaurantSuccessfully() {
        // Se simula que el feign client retorna "PROPIETARIO".
        when(userFeignPort.getUserRole(restaurant.getOwnerId())).thenReturn("PROPIETARIO");
        // Se simula que al guardar el restaurante se asigna un id.
        Restaurant savedRestaurant = new Restaurant(1L, restaurant.getName(), restaurant.getAddress(), restaurant.getOwnerId(), restaurant.getPhone(), restaurant.getLogoUrl(), restaurant.getNit());
        when(restaurantPersistencePort.save(restaurant)).thenReturn(savedRestaurant);

        Restaurant result = restaurantUseCase.createRestaurant(restaurant);

        verify(restaurantValidations, times(1)).validateRestaurant(restaurant);
        verify(userFeignPort, times(1)).getUserRole(restaurant.getOwnerId());
        verify(restaurantPersistencePort, times(1)).save(restaurant);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Restaurante Test", result.getName());
    }

    @Test
    public void testCreateRestaurantInvalidOwner() {
        // Si el feign client retorna un rol distinto a "PROPIETARIO", se debe lanzar excepción.
        when(userFeignPort.getUserRole(restaurant.getOwnerId())).thenReturn("CLIENTE");
        assertThrows(InvalidOwnerException.class, () -> restaurantUseCase.createRestaurant(restaurant));
        verify(restaurantValidations, times(1)).validateRestaurant(restaurant);
        verify(userFeignPort, times(1)).getUserRole(restaurant.getOwnerId());
    }
}