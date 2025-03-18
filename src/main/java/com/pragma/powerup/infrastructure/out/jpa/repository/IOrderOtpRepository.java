package com.pragma.powerup.infrastructure.out.jpa.repository;

import com.pragma.powerup.infrastructure.out.jpa.entity.OrderOtpEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IOrderOtpRepository extends JpaRepository<OrderOtpEntity, Long> {

    Optional<OrderOtpEntity> findByOrderId(Long orderId);
}
