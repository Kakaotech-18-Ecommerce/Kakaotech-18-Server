package com.kakaoteck.golagola.domain.cart.repository;

import com.kakaoteck.golagola.domain.cart.entity.Cart;
import com.kakaoteck.golagola.domain.cart.entity.CartProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartProductRepository extends JpaRepository<CartProduct, Long> {
}
