package com.kakaoteck.golagola.domain.product.repository;

import com.kakaoteck.golagola.domain.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
