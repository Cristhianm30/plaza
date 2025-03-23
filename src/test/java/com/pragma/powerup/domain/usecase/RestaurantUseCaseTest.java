package com.pragma.powerup.domain.usecase;



import com.pragma.powerup.application.dto.request.AssignEmployeeRequestDto;
import com.pragma.powerup.domain.exception.InvalidEmployeeException;
import com.pragma.powerup.domain.exception.InvalidOwnerException;
import com.pragma.powerup.domain.model.EmployeeRestaurant;
import com.pragma.powerup.domain.model.Restaurant;
import com.pragma.powerup.domain.model.Pagination;
import com.pragma.powerup.domain.spi.IEmployeeRestaurantPersistencePort;
import com.pragma.powerup.domain.spi.IRestaurantPersistencePort;
import com.pragma.powerup.domain.spi.IUserFeignPort;
import com.pragma.powerup.domain.usecase.validations.RestaurantValidations;
import com.pragma.powerup.domain.usecase.validations.TokenValidations;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
 class RestaurantUseCaseTest {

    @Mock
    private IRestaurantPersistencePort restaurantPersistencePort;
    @Mock
    private RestaurantValidations restaurantValidations;
    @Mock
    private IUserFeignPort userFeignPort;
    @Mock
    private TokenValidations tokenValidations;
    @Mock
    private IEmployeeRestaurantPersistencePort employeeRestaurantPersistencePort;

    @InjectMocks
    private RestaurantUseCase restaurantUseCase;

    private Restaurant restaurant;

    @BeforeEach
     void setup() {

        restaurant = new Restaurant(null, "Restaurante Test", "Calle 123", 100L, "+123456789", "logo.png", "123456");
    }

    @Test
     void testCreateRestaurantSuccessfully() {

        when(userFeignPort.getUserRole(restaurant.getOwnerId())).thenReturn("PROPIETARIO");

        Restaurant savedRestaurant = new Restaurant(1L, restaurant.getName(), restaurant.getAddress(),
                restaurant.getOwnerId(), restaurant.getPhone(), restaurant.getLogoUrl(), restaurant.getNit());
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
     void testCreateRestaurantInvalidOwner() {
        when(userFeignPort.getUserRole(restaurant.getOwnerId())).thenReturn("CLIENTE");


        doThrow(new InvalidOwnerException())
                .when(restaurantValidations).validateOwnerRole("CLIENTE");

        assertThrows(InvalidOwnerException.class, () ->
                restaurantUseCase.createRestaurant(restaurant)
        );
    }

    @Test
     void testGetAllRestaurantsPaginated() {
        int page = 0, size = 10;
        String sortBy = "name";

        Pagination pagination = new Pagination(Collections.emptyList(), 0, 0, 0L);

        when(restaurantPersistencePort.findAllPaginated(page, size, sortBy)).thenReturn(pagination);

        Pagination result = restaurantUseCase.getAllRestaurantsPaginated(page, size, sortBy);

        verify(restaurantPersistencePort, times(1)).findAllPaginated(page, size, sortBy);
        assertEquals(pagination, result);
    }

    @Test
     void testAssignEmployeeToRestaurantSuccessfully() {

        Long restaurantId = 1L;
        Long employeeId = 2L;
        AssignEmployeeRequestDto request = new AssignEmployeeRequestDto(employeeId);
        String token = "validToken";

        when(tokenValidations.cleanedToken(token)).thenReturn("cleanToken");
        when(userFeignPort.getUserRole(employeeId)).thenReturn("EMPLEADO");

        EmployeeRestaurant expected = new EmployeeRestaurant(1L, employeeId, restaurantId);
        when(employeeRestaurantPersistencePort.saveEmployee(any())).thenReturn(expected);


        EmployeeRestaurant result = restaurantUseCase.assignEmployeeToRestaurant(
                restaurantId,
                request,
                token
        );


        verify(tokenValidations).validateTokenAndOwnership("cleanToken", restaurantId);
        verify(restaurantValidations).validateEmployeeRole("EMPLEADO");
        assertEquals(expected, result);
    }

    @Test
     void testAssignEmployeeInvalidRole() {
        Long restaurantId = 1L;
        Long employeeId = 2L;
        AssignEmployeeRequestDto request = new AssignEmployeeRequestDto(employeeId);

        when(tokenValidations.cleanedToken(any())).thenReturn("cleanToken");
        when(userFeignPort.getUserRole(employeeId)).thenReturn("PROPIETARIO");


        doThrow(new InvalidEmployeeException())
                .when(restaurantValidations).validateEmployeeRole("PROPIETARIO");

        assertThrows(InvalidEmployeeException.class, () ->
                restaurantUseCase.assignEmployeeToRestaurant(restaurantId, request, "token")
        );
    }
}
