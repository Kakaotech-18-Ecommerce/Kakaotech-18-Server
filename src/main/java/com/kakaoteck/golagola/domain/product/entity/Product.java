package com.kakaoteck.golagola.domain.product.entity;

import com.kakaoteck.golagola.domain.cart.entity.Cart;
import com.kakaoteck.golagola.domain.orderProduct.entity.OrderProduct;
import com.kakaoteck.golagola.domain.review.entity.Review;
import com.kakaoteck.golagola.domain.seller.entity.Seller;
import com.kakaoteck.golagola.global.common.BaseEntity;
import com.kakaoteck.golagola.global.common.enums.Category;
import com.kakaoteck.golagola.global.common.enums.DetailCategory;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Table(name = "product_table")
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id")
    private Seller seller;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<Review> reviewList;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<OrderProduct> orderProductList;

    @Column(nullable = false)
    private String productName;

    @Column(nullable = false)
    private String productExplanation;

    @Column(nullable = false)
    private String productImage;

    @Column(nullable = false)
    private Long productPrice;

    @Column(nullable = false)
    private Long productInventory;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DetailCategory detailCategory;

    @Column(nullable = false)
    private Long discount;

    @Column(nullable = false)
    private LocalTime createTime;

    @Column(nullable = false)
    private LocalTime updateTime;

    @Column(nullable = false)
    private Long productQuantity;

    @Column(nullable = false)
    private Float predictReviewStar;

    @Column(nullable = false)
    private Float productStar;

    public static Product from(Long productId, Seller seller, Cart cart, List<Review> reviewList,
                               List<OrderProduct> orderProductList, String productName,
                               String productExplanation, String productImage, Long productPrice,
                               Long productInventory, Category category, DetailCategory detailCategory,
                               Long discount, LocalTime createTime, LocalTime updateTime,
                               Long productQuantity, Float predictReviewStar, Float productStar) {
        return Product.builder()
                .productId(productId)
                .seller(seller)
                .cart(cart)
                .reviewList(reviewList)
                .orderProductList(orderProductList)
                .productName(productName)
                .productExplanation(productExplanation)
                .productImage(productImage)
                .productPrice(productPrice)
                .productInventory(productInventory)
                .category(category)
                .detailCategory(detailCategory)
                .discount(discount)
                .createTime(createTime)
                .updateTime(updateTime)
                .productQuantity(productQuantity)
                .predictReviewStar(predictReviewStar)
                .productStar(productStar)
                .build();
    }

}
