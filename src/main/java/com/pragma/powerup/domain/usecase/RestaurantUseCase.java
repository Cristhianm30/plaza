package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.application.dto.request.AssignEmployeeRequestDto;
import com.pragma.powerup.domain.api.IRestaurantServicePort;
import com.pragma.powerup.domain.model.EmployeeRestaurant;
import com.pragma.powerup.domain.model.Pagination;
import com.pragma.powerup.domain.model.Restaurant;
import com.pragma.powerup.domain.spi.IEmployeeRestaurantPersistencePort;
import com.pragma.powerup.domain.spi.IRestaurantPersistencePort;
import com.pragma.powerup.domain.spi.IUserFeignPort;
import com.pragma.powerup.domain.usecase.validations.RestaurantValidations;
import com.pragma.powerup.domain.usecase.validations.TokenValidations;

public class RestaurantUseCase  implements IRestaurantServicePort {
    private final IRestaurantPersistencePort restaurantPersistencePort;
    private final RestaurantValidations restaurantValidations;
    private final IUserFeignPort userFeignPort;
    private final TokenValidations tokenValidations;
    private final IEmployeeRestaurantPersistencePort employeeRestaurantPersistencePort;


    public RestaurantUseCase(IRestaurantPersistencePort restaurantPersistencePort, RestaurantValidations restaurantValidations, IUserFeignPort userFeignPort, TokenValidations tokenValidations, IEmployeeRestaurantPersistencePort employeeRestaurantPersistencePort) {
        this.restaurantPersistencePort = restaurantPersistencePort;
        this.restaurantValidations = restaurantValidations;
        this.userFeignPort = userFeignPort;
        this.tokenValidations = tokenValidations;
        this.employeeRestaurantPersistencePort = employeeRestaurantPersistencePort;
    }

    @Override
    public Restaurant createRestaurant(Restaurant restaurant){

        restaurantValidations.validateRestaurant(restaurant);
        String userRole = userFeignPort.getUserRole(restaurant.getOwnerId());
        restaurantValidations.validateOwnerRole(userRole);
        return restaurantPersistencePort.save(restaurant);

    }

    @Override
    public Pagination<Restaurant> getAllRestaurantsPaginated(int page, int size, String sortBy) {
        return restaurantPersistencePort.findAllPaginated(page, size, sortBy);
    }

    @Override
    public EmployeeRestaurant assignEmployeeToRestaurant(Long idRestaurant, AssignEmployeeRequestDto assignEmployeeRequestDto, String token) {

        String cleanedToken = tokenValidations.cleanedToken(token);
        tokenValidations.validateTokenAndOwnership(cleanedToken,idRestaurant);
        Long employeeId = assignEmployeeRequestDto.getEmployeeId();
        String userRole = userFeignPort.getUserRole(employeeId);
        restaurantValidations.validateEmployeeRole(userRole);

        EmployeeRestaurant employeeRestaurant = new EmployeeRestaurant(null,employeeId,idRestaurant);

        return employeeRestaurantPersistencePort.saveEmployee(employeeRestaurant);
    }


}
