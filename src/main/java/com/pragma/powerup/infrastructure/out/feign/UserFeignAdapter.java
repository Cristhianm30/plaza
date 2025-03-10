package com.pragma.powerup.infrastructure.out.feign;

import com.pragma.powerup.application.dto.response.UserResponseDto;
import com.pragma.powerup.domain.spi.IUserFeignPort;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;


public class UserFeignAdapter implements IUserFeignPort {

    private final IUserFeignClient userFeignClient;

    public UserFeignAdapter(IUserFeignClient userFeignClient) {
        this.userFeignClient = userFeignClient;
    }

    @Override
    public String getUserRole(Long userId) {
        ResponseEntity<UserResponseDto> response = userFeignClient.getUserById(userId);
        UserResponseDto user = response.getBody();
        return (user != null && user.getRole() != null)
                ? user.getRole().getName()
                : null;
    }
}

