package com.pragma.powerup.infrastructure.out.jpa.adapter;

import com.pragma.powerup.domain.model.Order;
import com.pragma.powerup.domain.model.Pagination;
import com.pragma.powerup.domain.spi.IOrderPersistencePort;
import com.pragma.powerup.infrastructure.exception.RelationEmployeeRestaurantException;
import com.pragma.powerup.infrastructure.out.jpa.entity.OrderEntity;
import com.pragma.powerup.infrastructure.out.jpa.mapper.IOrderEntityMapper;
import com.pragma.powerup.infrastructure.out.jpa.repository.IOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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

    @Override
    public Pagination<Order> getOrdersByStatusAndRestaurant(String status, Long restaurantId, int page, int size) {

        Pageable pageable = PageRequest.of(page,size);
        Page<OrderEntity> orderPage = orderRepository.findByStatusAndRestaurantId(status,restaurantId,pageable);

        return new Pagination<>(
                orderPage.getContent().stream().map(orderEntityMapper::toModel).collect(Collectors.toList()),
                orderPage.getNumber(),
                orderPage.getTotalPages(),
                orderPage.getTotalElements()
        );
    }

    @Override
    public Order findById(Long id) {
        OrderEntity entity = orderRepository.findById(id).orElseThrow(RelationEmployeeRestaurantException::new);
        return orderEntityMapper.toModel(entity);
    }

    @Override
    public List<Order> findOrdersByRestaurantId(Long restaurantId) {

        List<OrderEntity> entityList = orderRepository.findOrdersByRestaurantId(restaurantId);


        return entityList.stream()
                .map(orderEntityMapper::toModel)
                .collect(Collectors.toList());
    }
}
