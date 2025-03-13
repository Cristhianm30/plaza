package com.pragma.powerup.domain.usecase.validations;

import com.pragma.powerup.domain.exception.InvalidOwnerException;
import com.pragma.powerup.domain.exception.InvalidTokenException;
import com.pragma.powerup.domain.model.Restaurant;
import com.pragma.powerup.domain.spi.IJwtTokenProviderPort;
import com.pragma.powerup.domain.spi.IRestaurantPersistencePort;

public class TokenValidations {
    private final IJwtTokenProviderPort jwtTokenProvider;
    private final IRestaurantPersistencePort restaurantPersistence;

    public TokenValidations(IJwtTokenProviderPort jwtTokenProvider, IRestaurantPersistencePort restaurantPersistence) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.restaurantPersistence = restaurantPersistence;
    }

    public void validateTokenAndOwnership(String token, Long restaurantId) {
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

        // Validar propiedad del restaurante
        Restaurant restaurant = restaurantPersistence.findById(restaurantId);
        if (!restaurant.getOwnerId().equals(ownerId)) {
            throw new InvalidOwnerException();
        }
    }
}
