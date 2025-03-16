package com.pragma.powerup.infrastructure.out.jpa.adapter;

import com.pragma.powerup.domain.model.Order;
import com.pragma.powerup.domain.spi.IOrderPersistencePort;
import com.pragma.powerup.infrastructure.out.jpa.entity.OrderEntity;
import com.pragma.powerup.infrastructure.out.jpa.mapper.IOrderEntityMapper;
import com.pragma.powerup.infrastructure.out.jpa.repository.IOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
public class OrderJpaAdapter implements IOrderPersistencePort {

    private final IOrderRepository orderRepository;
    private final IOrderEntityMapper orderEntityMapper;


    @Override
    @Transactional
    public Order saveOrder(Order order) {
        OrderEntity entity = orderEntityMapper.toEntity(order);
        entity.getDishes().forEach(dish -> dish.setOrder(entity));
        OrderEntity savedEntity = orderRepository.save(entity);
        return orderEntityMapper.toModel(savedEntity);
    }

    @Override
    public boolean existsActiveOrder(Long clientId) {
        List<String> activeStatuses = List.of("PENDIENTE", "EN_PREPARACION", "LISTO");
        return orderRepository.existsByClientIdAndStatusIn(clientId, activeStatuses);
    }
}
