package com.pragma.powerup.infrastructure.out.feign;

import com.pragma.powerup.application.dto.response.UserResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", url = "http://localhost:8080")
public interface IUserFeignClient {

    @GetMapping("/users/{id}")
    ResponseEntity<UserResponseDto> getUserById(@PathVariable("id") Long userId);
}
