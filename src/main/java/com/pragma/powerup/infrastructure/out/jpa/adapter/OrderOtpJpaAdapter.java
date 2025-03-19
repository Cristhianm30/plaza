package com.pragma.powerup.infrastructure.out.jpa.adapter;

import com.pragma.powerup.domain.model.OrderOtp;
import com.pragma.powerup.domain.spi.IOrderOtpPersistencePort;
import com.pragma.powerup.infrastructure.exception.OrderOtpNotFoundException;
import com.pragma.powerup.infrastructure.out.jpa.entity.OrderOtpEntity;
import com.pragma.powerup.infrastructure.out.jpa.mapper.IOrderOtpEntityMapper;
import com.pragma.powerup.infrastructure.out.jpa.repository.IOrderOtpRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OrderOtpJpaAdapter implements IOrderOtpPersistencePort {

    private final IOrderOtpRepository orderOtpRepository;
    private final IOrderOtpEntityMapper orderOtpEntityMapper;

    @Override
    public OrderOtp saveOrderOtp(OrderOtp orderOtp) {

        OrderOtpEntity entity = orderOtpEntityMapper.toEntity(orderOtp);
        OrderOtpEntity savedEntity = orderOtpRepository.save(entity);

        return orderOtpEntityMapper.toModel(savedEntity);
    }

    @Override
    public OrderOtp findByOrderId(Long orderId) {
        OrderOtpEntity entity = orderOtpRepository.findByOrderId(orderId).orElseThrow(OrderOtpNotFoundException::new);
        return orderOtpEntityMapper.toModel(entity);
    }
}
