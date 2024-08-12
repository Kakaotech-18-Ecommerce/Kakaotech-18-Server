package com.kakaoteck.golagola.domain.orderProduct.entity;

import com.kakaoteck.golagola.domain.order.entity.Order;
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
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id"m nullable = false)
    private Order order;

    @OneToOne(mappedBy = "orderProduct", cascade = CascadeType.ALL)
    private Review review;

    @Column(nullable = false)
    private Long quantity;

    @Column(nullable = false)
    private Long orderPrice;

    public static OrderProduct from(Long quantity, Long orderPrice) {
        return OrderProduct.builder()
                .quantity(quantity)
                .orderPrice(orderPrice)
                .build();
    }
}
