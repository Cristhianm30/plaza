package com.pragma.powerup.infrastructure.out.feign;

import com.pragma.powerup.application.dto.request.OtpRequestDto;
import com.pragma.powerup.application.dto.response.OtpResponseDto;
import com.pragma.powerup.domain.model.OrderOtp;
import com.pragma.powerup.domain.spi.IMessagingFeignPort;
import com.pragma.powerup.infrastructure.exception.OrderOtpNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class MessagingFeignAdapter implements IMessagingFeignPort {

    private final IMessagingFeignClient messagingFeignClient;

    public MessagingFeignAdapter(IMessagingFeignClient messagingFeignClient) {
        this.messagingFeignClient = messagingFeignClient;
    }

    @Override
    public OrderOtp sendOtp(String phone, Long orderId) {
        OtpRequestDto request = new OtpRequestDto();
        request.setPhone(phone);
        ResponseEntity<OtpResponseDto> response = messagingFeignClient.sendOtp(request);
        OtpResponseDto dto = response.getBody();
        if (dto == null || dto.getOtp() == null || dto.getOtp().isBlank()) {
            throw new OrderOtpNotFoundException();
        }
        return new OrderOtp(orderId,dto.getOtp());
    }
}
