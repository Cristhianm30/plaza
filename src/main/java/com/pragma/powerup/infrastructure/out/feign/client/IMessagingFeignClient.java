package com.pragma.powerup.infrastructure.out.feign.client;

import com.pragma.powerup.application.dto.request.OtpRequestDto;
import com.pragma.powerup.application.dto.response.OtpResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "messaging-service", url = "${messaging-service.url}")
public interface IMessagingFeignClient {

    @PostMapping("/messaging/otp")
    ResponseEntity<OtpResponseDto> sendOtp(@RequestBody OtpRequestDto otpRequestDto);
}
