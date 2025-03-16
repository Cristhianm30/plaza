package com.pragma.powerup.infrastructure.configuration;

import com.pragma.powerup.domain.api.IDishServicePort;
import com.pragma.powerup.domain.api.IObjectServicePort;
import com.pragma.powerup.domain.api.IOrderServicePort;
import com.pragma.powerup.domain.api.IRestaurantServicePort;
import com.pragma.powerup.domain.spi.*;
import com.pragma.powerup.domain.usecase.DishUseCase;
import com.pragma.powerup.domain.usecase.ObjectUseCase;
import com.pragma.powerup.domain.usecase.OrderUseCase;
import com.pragma.powerup.domain.usecase.RestaurantUseCase;
import com.pragma.powerup.domain.usecase.validations.DishValidations;
import com.pragma.powerup.domain.usecase.validations.OrderValidations;
import com.pragma.powerup.domain.usecase.validations.RestaurantValidations;
import com.pragma.powerup.domain.usecase.validations.TokenValidations;
import com.pragma.powerup.infrastructure.out.feign.IUserFeignClient;
import com.pragma.powerup.infrastructure.out.feign.UserFeignAdapter;

import com.pragma.powerup.infrastructure.out.jpa.adapter.*;
import com.pragma.powerup.infrastructure.out.jpa.mapper.*;
import com.pragma.powerup.infrastructure.out.jpa.repository.*;

import com.pragma.powerup.infrastructure.security.JwtTokenProviderAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@RequiredArgsConstructor
public class BeanConfiguration {
    private final IObjectRepository objectRepository;
    private final IObjectEntityMapper objectEntityMapper;
    private final IRestaurantRepository restaurantRepository;
    private final IRestaurantEntityMapper restaurantEntityMapper;
    private final IDishEntityMapper dishEntityMapper;
    private final IDishRepository dishRepository;
    private final ICategoryRepository categoryRepository;
    private final ICategoryEntityMapper categoryEntityMapper;
    private final IOrderRepository orderRepository;
    private final IOrderEntityMapper orderEntityMapper;



    @Bean
    public IObjectPersistencePort objectPersistencePort(IObjectEntityMapper objectEntityMapper) {
        return new ObjectJpaAdapter(objectRepository, objectEntityMapper);
    }

    @Bean
    public IObjectServicePort objectServicePort(IObjectPersistencePort objectPersistencePort) {
        return new ObjectUseCase(objectPersistencePort);
    }


    @Bean
    public IUserFeignPort userFeignPort(IUserFeignClient userFeignClient) {
        return new UserFeignAdapter(userFeignClient);
    }


    @Bean
    public IRestaurantServicePort iRestaurantServicePort(
            IRestaurantPersistencePort restaurantPersistencePort,
            IUserFeignPort userFeignPort,RestaurantValidations restaurantValidations
    ) {
        return new RestaurantUseCase(restaurantPersistencePort,restaurantValidations, userFeignPort);
    }

    @Bean
    public IRestaurantPersistencePort iRestaurantPersistencePort() {
        return new RestaurantJpaAdapter(restaurantEntityMapper, restaurantRepository);
    }

    @Bean
    public RestaurantValidations restaurantValidations(){
        return new RestaurantValidations();
    }

    @Bean
    public IJwtTokenProviderPort jwtTokenProvider() {
        return new JwtTokenProviderAdapter();
    }

    @Bean
    public IDishServicePort dishServicePort(
            IDishPersistencePort dishPersistencePort,
            DishValidations dishValidations,
            TokenValidations tokenValidations,
            IRestaurantPersistencePort restaurantPersistencePort,
            ICategoryPersistencePort categoryPersistencePort) {

        return new DishUseCase(
                dishPersistencePort,
                dishValidations,
                tokenValidations,
                restaurantPersistencePort,
                categoryPersistencePort
        );
    }

    @Bean
    public IDishPersistencePort dishPersistencePort() {
        return new DishJpaAdapter(dishEntityMapper,dishRepository);
    }

    @Bean
    public DishValidations dishValidations() {
        return new DishValidations();
    }

    @Bean
    public ICategoryPersistencePort categoryPersistencePort() {
        return new CategoryJpaAdapter(categoryEntityMapper, categoryRepository);
    }

    @Bean
    public TokenValidations tokenValidations(
            IJwtTokenProviderPort jwtTokenProvider,
            IRestaurantPersistencePort restaurantPersistence
    ) {
        return new TokenValidations(jwtTokenProvider, restaurantPersistence);
    }

    @Bean
    public IOrderServicePort orderServicePort(
            IOrderPersistencePort orderPersistencePort,
            TokenValidations tokenValidations,
            OrderValidations orderValidations){
        return new OrderUseCase(orderPersistencePort,tokenValidations,orderValidations);
    }

    @Bean
    public IOrderPersistencePort orderPersistencePort(){
        return new OrderJpaAdapter(orderRepository,orderEntityMapper);
    }

    @Bean
    public OrderValidations orderValidations(
            IOrderPersistencePort orderPersistence,
            IDishPersistencePort dishPersistence
    ) {
        return new OrderValidations(orderPersistence, dishPersistence);
    }





}