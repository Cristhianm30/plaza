package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.exception.InvalidEmployeeException;
import com.pragma.powerup.domain.model.*;
import com.pragma.powerup.domain.spi.IEmployeeRestaurantPersistencePort;
import com.pragma.powerup.domain.spi.IOrderPersistencePort;
import com.pragma.powerup.domain.spi.IUserFeignPort;
import com.pragma.powerup.domain.usecase.validations.OrderValidations;
import com.pragma.powerup.domain.usecase.validations.TokenValidations;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderUseCaseTest {

    @Mock
    private IOrderPersistencePort orderPersistencePort;
    @Mock
    private TokenValidations tokenValidations;
    @Mock
    private OrderValidations orderValidations;
    @Mock
    private IUserFeignPort userFeignPort;
    @Mock
    private IEmployeeRestaurantPersistencePort employeeRestaurantPersistencePort;

    @InjectMocks
    private OrderUseCase orderUseCase;

    private final String validToken = "Bearer token";
    private final Long userId = 1L;



    @Test
    void testCreateOrderSuccessfully() {
        // Crear objetos necesarios
        Restaurant restaurant = new Restaurant(1L, "Test", "Address", 1L, "123", "logo.jpg", "NIT123");
        List<OrderDish> dishes = new ArrayList<>();

        // Order inicial - parámetros en ORDEN CORRECTO según el constructor
        Order initialOrder = new Order(
                null,       // id
                null,       // clientId (se seteará desde token)
                null,       // chefId
                restaurant, // Debe ser objeto Restaurant (NO Long)
                null,       // status
                null,       // date
                dishes
        );

        // Mockear comportamiento
        when(tokenValidations.cleanedToken(validToken)).thenReturn("token");
        when(tokenValidations.getUserIdFromToken("token")).thenReturn(userId);

        // Order esperado
        Order expectedOrder = new Order(
                1L,
                userId,
                null,
                restaurant,
                "PENDIENTE",
                LocalDateTime.now(),
                dishes
        );
        when(orderPersistencePort.saveOrder(any(Order.class))).thenReturn(expectedOrder);

        // Ejecutar
        Order result = orderUseCase.createOrder(initialOrder, validToken);

        // Verificaciones
        assertNotNull(result.getId());
        assertEquals("PENDIENTE", result.getStatus());
        assertEquals(userId, result.getClientId());
        assertEquals(restaurant.getId(), result.getRestaurant().getId()); // Validar relación
    }

    @Test
    void testGetAllOrdersPaginatedSuccessfully() {
        String status = "PENDIENTE";
        int page = 0;
        int size = 10;
        Long restaurantId = 1L;

        when(tokenValidations.cleanedToken(validToken)).thenReturn("token");
        when(tokenValidations.getUserIdFromToken("token")).thenReturn(userId);
        when(userFeignPort.getUserRole(userId)).thenReturn("EMPLEADO");
        when(employeeRestaurantPersistencePort.findByEmployeeId(userId))
                .thenReturn(new EmployeeRestaurant(1L, userId, restaurantId));

        Pagination<Order> expectedPagination = new Pagination<>(Collections.emptyList(), page, size, 0L);
        when(orderPersistencePort.getOrdersByStatusAndRestaurant(status, restaurantId, page, size))
                .thenReturn(expectedPagination);

        Pagination<Order> result = orderUseCase.getAllOrdersPaginated(status, page, size, validToken);

        verify(orderValidations).validateEmployeeRole("EMPLEADO");
        assertEquals(expectedPagination, result);
    }

    @Test
    void testGetAllOrdersPaginatedInvalidRole() {
        // Configurar mocks
        String cleanedToken = "cleanedToken";
        Long employeeId = 1L;

        when(tokenValidations.cleanedToken(validToken)).thenReturn(cleanedToken);
        when(tokenValidations.getUserIdFromToken(cleanedToken)).thenReturn(employeeId);
        when(userFeignPort.getUserRole(employeeId)).thenReturn("CLIENTE");

        // Simular que la validación del rol lanza la excepción
        doThrow(new InvalidEmployeeException())
                .when(orderValidations).validateEmployeeRole("CLIENTE");

        // Verificar que se lanza la excepción esperada
        assertThrows(InvalidEmployeeException.class, () ->
                orderUseCase.getAllOrdersPaginated("PENDIENTE", 0, 10, validToken)
        );

        // Verificar que NO se intenta obtener el restaurante del empleado
        verify(employeeRestaurantPersistencePort, never()).findByEmployeeId(any());
    }
}
