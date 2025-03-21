package com.pragma.powerup.infrastructure.out.jpa.repository;


import com.pragma.powerup.infrastructure.out.jpa.entity.OrderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface IOrderRepository extends JpaRepository<OrderEntity, Long> {

    boolean existsByClientIdAndStatusIn(Long clientId, List<String> statuses);

    Page<OrderEntity> findByStatusAndRestaurantId(String status, Long restaurantId, Pageable pageable);

    Optional<OrderEntity> findById (Long orderId);

    List<OrderEntity> findOrdersByRestaurantId (Long restaurantId);

}
