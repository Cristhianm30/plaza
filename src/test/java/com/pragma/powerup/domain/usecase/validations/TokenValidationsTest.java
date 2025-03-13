package com.pragma.powerup.domain.usecase.validations;

import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

import com.pragma.powerup.domain.exception.InvalidOwnerException;
import com.pragma.powerup.domain.exception.InvalidTokenException;
import com.pragma.powerup.domain.model.Restaurant;
import com.pragma.powerup.domain.spi.IJwtTokenProviderPort;
import com.pragma.powerup.domain.spi.IRestaurantPersistencePort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TokenValidationsTest {

    @Mock
    private IJwtTokenProviderPort jwtTokenProvider;
    @Mock
    private IRestaurantPersistencePort restaurantPersistence;

    private TokenValidations tokenValidations;
    private final String validToken = "validToken";
    private final Long restaurantId = 1L;
    private Restaurant restaurant;

    @BeforeEach
    public void setup() {
        tokenValidations = new TokenValidations(jwtTokenProvider, restaurantPersistence);
        // Se crea un restaurante con ownerId = 100L
        restaurant = new Restaurant(restaurantId, "Restaurante Test", "Calle 123", 100L, "+123456789", "logo.png", "123456");
    }

    @Test
    public void testValidateTokenAndOwnershipSuccessfully() {
        when(jwtTokenProvider.validateToken(validToken)).thenReturn(true);
        when(jwtTokenProvider.getRoleFromToken(validToken)).thenReturn("PROPIETARIO");
        when(jwtTokenProvider.getUserIdFromToken(validToken)).thenReturn(100L);
        when(restaurantPersistence.findById(restaurantId)).thenReturn(restaurant);

        // No debe lanzar excepción
        tokenValidations.validateTokenAndOwnership(validToken, restaurantId);

        verify(jwtTokenProvider, times(1)).validateToken(validToken);
        verify(jwtTokenProvider, times(1)).getRoleFromToken(validToken);
        verify(jwtTokenProvider, times(1)).getUserIdFromToken(validToken);
        verify(restaurantPersistence, times(1)).findById(restaurantId);
    }

    @Test
    public void testValidateTokenInvalid() {
        when(jwtTokenProvider.validateToken(validToken)).thenReturn(false);
        assertThrows(InvalidTokenException.class, () -> tokenValidations.validateTokenAndOwnership(validToken, restaurantId));
    }

    @Test
    public void testValidateTokenWrongRole() {
        when(jwtTokenProvider.validateToken(validToken)).thenReturn(true);
        when(jwtTokenProvider.getRoleFromToken(validToken)).thenReturn("CLIENTE");
        assertThrows(InvalidOwnerException.class, () -> tokenValidations.validateTokenAndOwnership(validToken, restaurantId));
    }

    @Test
    public void testValidateTokenWrongOwner() {
        when(jwtTokenProvider.validateToken(validToken)).thenReturn(true);
        when(jwtTokenProvider.getRoleFromToken(validToken)).thenReturn("PROPIETARIO");
        // Simulamos que el token indica un ownerId distinto al del restaurante
        when(jwtTokenProvider.getUserIdFromToken(validToken)).thenReturn(200L);
        when(restaurantPersistence.findById(restaurantId)).thenReturn(restaurant);

        assertThrows(InvalidOwnerException.class, () -> tokenValidations.validateTokenAndOwnership(validToken, restaurantId));
    }
}