package com.pragma.powerup.infrastructure.configuration;

import com.pragma.powerup.domain.api.IObjectServicePort;
import com.pragma.powerup.domain.api.IRestaurantServicePort;
import com.pragma.powerup.domain.spi.IObjectPersistencePort;
import com.pragma.powerup.domain.spi.IRestaurantPersistencePort;
import com.pragma.powerup.domain.spi.IUserFeignPort;
import com.pragma.powerup.domain.usecase.ObjectUseCase;
import com.pragma.powerup.domain.usecase.RestaurantUseCase;
import com.pragma.powerup.domain.usecase.validations.RestaurantValidations;
import com.pragma.powerup.infrastructure.out.feign.IUserFeignClient;
import com.pragma.powerup.infrastructure.out.feign.UserFeignAdapter;
import com.pragma.powerup.infrastructure.out.jpa.adapter.ObjectJpaAdapter;
import com.pragma.powerup.infrastructure.out.jpa.adapter.RestaurantJpaAdapter;
import com.pragma.powerup.infrastructure.out.jpa.mapper.IObjectEntityMapper;
import com.pragma.powerup.infrastructure.out.jpa.mapper.IRestaurantEntityMapper;
import com.pragma.powerup.infrastructure.out.jpa.repository.IObjectRepository;
import com.pragma.powerup.infrastructure.out.jpa.repository.IRestaurantRepository;
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
}