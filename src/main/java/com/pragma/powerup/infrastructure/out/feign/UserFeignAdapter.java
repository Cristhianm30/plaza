package com.pragma.powerup.infrastructure.out.feign;


import com.pragma.powerup.application.dto.response.UserResponseDto;
import com.pragma.powerup.domain.spi.IUserFeignPort;

import org.springframework.http.ResponseEntity;



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

    @Override
    public String getUserPhone(Long userId) {
        ResponseEntity<UserResponseDto> response = userFeignClient.getUserById(userId);
        UserResponseDto user = response.getBody();
        String phone = (user != null) ? user.getCellPhone() : null;
        return phone;
    }

}

