package com.pragma.powerup.domain.usecase.validations;

import com.pragma.powerup.domain.exception.InvalidOwnerException;
import com.pragma.powerup.domain.exception.InvalidRestaurantOwnerException;
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

        if (!jwtTokenProvider.validateToken(token)) {
            throw new InvalidTokenException();
        }


        String role = jwtTokenProvider.getRoleFromToken(token);
        Long ownerId = jwtTokenProvider.getUserIdFromToken(token);


        if (!"PROPIETARIO".equals(role)) {
            throw new InvalidOwnerException();
        }


        Restaurant restaurant = restaurantPersistence.findById(restaurantId);
        if (!restaurant.getOwnerId().equals(ownerId)) {
            throw new InvalidRestaurantOwnerException();
        }
    }

    public String  cleanedToken (String token){
        return token.replace("Bearer ", "");
    }

    public Long getUserIdFromToken (String token){
        return jwtTokenProvider.getUserIdFromToken(token);
    }
}
