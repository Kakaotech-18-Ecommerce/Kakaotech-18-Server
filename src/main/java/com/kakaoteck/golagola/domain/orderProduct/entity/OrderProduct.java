package com.kakaoteck.golagola.domain.orderProduct.entity;

import com.kakaoteck.golagola.domain.product.entity.Product;
import com.kakaoteck.golagola.domain.review.entity.Review;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Table(name = "order_product_table")
public class OrderProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderProductId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @OneToOne(mappedBy = "orderProduct", cascade = CascadeType.ALL)
    private Review review;

    private Long quantity;
    private Long orderPrice;
}
