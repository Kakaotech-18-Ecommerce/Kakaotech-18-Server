package com.kakaoteck.golagola.domain.order.repository;

import com.kakaoteck.golagola.domain.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
