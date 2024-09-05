package com.kakaoteck.golagola.domain.review.entity;

import com.kakaoteck.golagola.domain.buyer.entity.Buyer;
import com.kakaoteck.golagola.domain.orderProduct.entity.OrderProduct;
import com.kakaoteck.golagola.domain.product.entity.Product;
import com.kakaoteck.golagola.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Table(name = "review_table")
public class Review extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_id", nullable = false)
    private Buyer buyer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_product_id")
    private OrderProduct orderProduct;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    @Column(nullable = false)
    private String reviewTitle;

    @Column(nullable = false)
    private Long reviewStar;

    @Column(nullable = false)
    private String reviewContent;

    @Column(nullable = false)
    private LocalDate reviewCreateTime;

    @Column(nullable = false)
    private LocalDate reviewUpdateTime;

    public static Review from(Long reviewId, Buyer buyer, Product product, OrderProduct orderProduct,
                              String reviewTitle, Long reviewStar, String reviewContent,
                              LocalDate reviewCreateTime, LocalDate reviewUpdateTime) {
        return Review.builder()
                .reviewId(reviewId)
                .buyer(buyer)
                .product(product)
                .orderProduct(orderProduct)
                .reviewTitle(reviewTitle)
                .reviewStar(reviewStar)
                .reviewContent(reviewContent)
                .reviewCreateTime(reviewCreateTime)
                .reviewUpdateTime(reviewUpdateTime)
                .build();
    }
}
