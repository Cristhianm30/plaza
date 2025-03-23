package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.exception.*;
import com.pragma.powerup.domain.model.*;
import com.pragma.powerup.domain.spi.*;
import com.pragma.powerup.domain.usecase.validations.OrderValidations;
import com.pragma.powerup.domain.usecase.validations.TokenValidations;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
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

    @Mock
    private IMessagingFeignPort messagingFeignPort;

    @Mock
    private IOrderOtpPersistencePort orderOtpPersistencePort;

    @Mock
    private ITraceabilityFeignPort traceabilityFeignPort;

    @Mock
    private IRestaurantPersistencePort restaurantPersistencePort;

    @InjectMocks
    private OrderUseCase orderUseCase;


    @Test
    void createOrder_ShouldSaveValidOrder() {
        // Arrange
        Order order = new Order();
        String token = "token";
        Long clientId = 100L;

        when(tokenValidations.cleanedToken(token)).thenReturn("clean_token");
        when(tokenValidations.getUserIdFromToken("clean_token")).thenReturn(clientId);
        when(userFeignPort.getUserEmail(clientId)).thenReturn("client@test.com");
        when(orderPersistencePort.saveOrder(any())).thenReturn(order);


        Order result = orderUseCase.createOrder(order, token);


        verify(orderValidations).validateActiveOrders(clientId);
        verify(orderValidations).validateDishesBelongToRestaurant(order);
        assertThat(result.getStatus()).isEqualTo("PENDIENTE");
        assertThat(result.getDate()).isNotNull();
    }

    @Test
    void createOrder_ShouldThrowWhenActiveOrderExists() {

        Order order = new Order();
        String token = "token";
        Long clientId = 100L;

        when(tokenValidations.cleanedToken(token)).thenReturn("clean_token");
        when(tokenValidations.getUserIdFromToken("clean_token")).thenReturn(clientId);
        doThrow(new HasActiveOrderException()).when(orderValidations).validateActiveOrders(clientId);


        assertThatThrownBy(() -> orderUseCase.createOrder(order, token))
                .isInstanceOf(HasActiveOrderException.class);
    }


    @Test
    void getAllOrdersPaginated_ShouldReturnFilteredOrders() {

        String token = "token";
        Long employeeId = 200L;
        Long restaurantId = 200L;
        EmployeeRestaurant employee = new EmployeeRestaurant(1L, employeeId, restaurantId);
        Pagination<Order> expectedPagination = new Pagination<>(
                Collections.emptyList(),
                0,
                0,
                0L
        );

        when(tokenValidations.cleanedToken(token)).thenReturn("clean_token");
        when(tokenValidations.getUserIdFromToken("clean_token")).thenReturn(employeeId);
        when(employeeRestaurantPersistencePort.findByEmployeeId(employeeId)).thenReturn(employee);
        when(orderPersistencePort.getOrdersByStatusAndRestaurant("PENDIENTE", restaurantId, 0, 10))
                .thenReturn(expectedPagination);


        Pagination<Order> result = orderUseCase.getAllOrdersPaginated("PENDIENTE", 0, 10, token);


        assertThat(result.getItems()).isEmpty();
    }


    @Test
    void assignEmployeeToOrder_ShouldUpdateStatusAndChef() {

        Long orderId = 1L;
        String token = "token";
        Long employeeId = 200L;
        Long restaurantId = 300L;


        Restaurant restaurant = new Restaurant();
        restaurant.setId(restaurantId);

        Order order = new Order();
        order.setStatus("PENDIENTE");
        order.setRestaurant(restaurant);

        EmployeeRestaurant employeeRestaurant = new EmployeeRestaurant(1L, employeeId, restaurantId);

        when(tokenValidations.cleanedToken(token)).thenReturn("clean_token");
        when(tokenValidations.getUserIdFromToken("clean_token")).thenReturn(employeeId);
        when(orderPersistencePort.findById(orderId)).thenReturn(order);
        when(employeeRestaurantPersistencePort.findByEmployeeId(employeeId)).thenReturn(employeeRestaurant);
        when(orderPersistencePort.saveOrder(order)).thenReturn(order);


        Order result = orderUseCase.assignEmployeeToOrder(orderId, token);


        assertThat(result.getStatus()).isEqualTo("EN_PREPARACION");
        assertThat(result.getChefId()).isEqualTo(employeeId);
        verify(orderValidations).updateTraceability(any(), any(), any());
    }




    @Test
    void deliverOrder_ShouldValidateOtpAndUpdateStatus() {

        Long orderId = 1L;
        String otpCode = "1234";
        String token = "token";
        Long employeeId = 200L;
        Order order = new Order();
        order.setStatus("LISTO");
        OrderOtp orderOtp = new OrderOtp(orderId, otpCode );
        Order savedOrder = new Order();
        savedOrder.setStatus("ENTREGADO");

        when(tokenValidations.cleanedToken(token)).thenReturn("clean_token");
        when(tokenValidations.getUserIdFromToken("clean_token")).thenReturn(employeeId);
        when(orderPersistencePort.findById(orderId)).thenReturn(order);
        when(orderOtpPersistencePort.findByOrderId(orderId)).thenReturn(orderOtp);
        when(orderPersistencePort.saveOrder(order)).thenReturn(savedOrder);


        Order result = orderUseCase.deliverOrder(orderId, otpCode, token);


        assertThat(result.getStatus()).isEqualTo("ENTREGADO");
    }


    @Test
    void cancelOrder_ShouldUpdateStatusWhenValid() {

        Long orderId = 1L;
        String token = "token";
        Long clientId = 100L;
        Order order = new Order();
        order.setStatus("PENDIENTE");
        order.setClientId(clientId);
        Order savedOrder = new Order();
        savedOrder.setStatus("CANCELADO");

        when(tokenValidations.cleanedToken(token)).thenReturn("clean_token");
        when(tokenValidations.getUserIdFromToken("clean_token")).thenReturn(clientId);
        when(orderPersistencePort.findById(orderId)).thenReturn(order);
        when(orderPersistencePort.saveOrder(order)).thenReturn(savedOrder);


        Order result = orderUseCase.cancelOrder(orderId, token);


        assertThat(result.getStatus()).isEqualTo("CANCELADO");
    }





    @Test
    void notifyOrderReady_ShouldUpdateStatusAndSendOtp() {

        Long orderId = 1L;
        String token = "token";
        Long employeeId = 200L;
        Order order = new Order();
        order.setId(orderId);
        order.setStatus("EN_PREPARACION");
        order.setChefId(employeeId);
        String clientPhone = "1234567890";
        OrderOtp orderOtp = new OrderOtp(orderId, "1234");

        when(tokenValidations.cleanedToken(token)).thenReturn("clean_token");
        when(tokenValidations.getUserIdFromToken("clean_token")).thenReturn(employeeId);
        when(orderPersistencePort.findById(orderId)).thenReturn(order);
        when(userFeignPort.getUserPhone(order.getClientId())).thenReturn(clientPhone);
        when(messagingFeignPort.sendOtp(clientPhone, orderId)).thenReturn(orderOtp);
        when(orderPersistencePort.saveOrder(order)).thenReturn(order);


        Order result = orderUseCase.notifyOrderReady(orderId, token);


        assertThat(result.getStatus()).isEqualTo("LISTO");
        verify(orderOtpPersistencePort).saveOrderOtp(orderOtp);
        verify(orderValidations).updateTraceability(any(), any(), any());
    }



    @Test
    void getTraceabilityByClient_ShouldReturnLogs() {

        String token = "token";
        Long clientId = 100L;
        List<Traceability> expectedLogs = List.of(new Traceability());

        when(tokenValidations.cleanedToken(token)).thenReturn("clean_token");
        when(tokenValidations.getUserIdFromToken("clean_token")).thenReturn(clientId);
        when(traceabilityFeignPort.getTraceabilityByClient(clientId)).thenReturn(expectedLogs);


        List<Traceability> result = orderUseCase.getTraceabilityByClient(token);


        assertThat(result).hasSize(1);
    }


    @Test
    void getOrdersEfficiency_ShouldReturnEfficiencyData() {

        String token = "token";
        Long ownerId = 500L;
        Restaurant restaurant = new Restaurant();
        restaurant.setId(300L);
        List<Order> orders = List.of(new Order(), new Order());

        when(tokenValidations.cleanedToken(token)).thenReturn("clean_token");
        when(tokenValidations.getUserIdFromToken("clean_token")).thenReturn(ownerId);
        when(restaurantPersistencePort.findRestaurantByOwnerId(ownerId)).thenReturn(restaurant);
        when(orderPersistencePort.findOrdersByRestaurantId(restaurant.getId())).thenReturn(orders);
        when(traceabilityFeignPort.getOrderEfficiency(any())).thenReturn(List.of(new OrderEfficiency()));


        List<OrderEfficiency> result = orderUseCase.getOrdersEfficiency(token);


        assertThat(result).isNotEmpty();
        verify(traceabilityFeignPort).getOrderEfficiency(any());
    }

    @Test
    void getOrdersEfficiency_ShouldThrowWhenNoOrders() {

        String token = "token";
        Restaurant restaurant = new Restaurant();

        when(tokenValidations.getUserIdFromToken(any())).thenReturn(1L);
        when(restaurantPersistencePort.findRestaurantByOwnerId(any())).thenReturn(restaurant);
        when(orderPersistencePort.findOrdersByRestaurantId(any())).thenReturn(Collections.emptyList());


        assertThatThrownBy(() -> orderUseCase.getOrdersEfficiency(token))
                .isInstanceOf(OrdersNotFoundException.class);
    }


    @Test
    void getEmployeeRanking_ShouldReturnRanking() {

        String token = "token";
        Long ownerId = 500L;
        Restaurant restaurant = new Restaurant();
        restaurant.setId(300L);
        List<Order> orders = List.of(new Order());

        when(tokenValidations.cleanedToken(token)).thenReturn("clean_token");
        when(tokenValidations.getUserIdFromToken("clean_token")).thenReturn(ownerId);
        when(restaurantPersistencePort.findRestaurantByOwnerId(ownerId)).thenReturn(restaurant);
        when(orderPersistencePort.findOrdersByRestaurantId(restaurant.getId())).thenReturn(orders);
        when(traceabilityFeignPort.getEmployeeRanking(any())).thenReturn(List.of(new EmployeeRanking()));


        List<EmployeeRanking> result = orderUseCase.getEmployeeRanking(token);


        assertThat(result).isNotEmpty();
        verify(traceabilityFeignPort).getEmployeeRanking(any());
    }
}
