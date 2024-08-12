package com.kakaoteck.golagola.domain.orderProduct.repository;

import com.kakaoteck.golagola.domain.orderProduct.entity.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {

}
