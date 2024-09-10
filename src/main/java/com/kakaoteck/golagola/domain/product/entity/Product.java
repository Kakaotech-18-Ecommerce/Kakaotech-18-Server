package com.kakaoteck.golagola.domain.product.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.kakaoteck.golagola.domain.cart.entity.CartProduct;
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
import lombok.experimental.SuperBuilder;

import java.time.LocalTime;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Table(name = "product_table")
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id")
    @JsonBackReference
    private Seller seller;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartProduct> cartProducts;

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

//    @Enumerated(EnumType.STRING)
//    @Column(nullable = false)
//    private DetailCategory detailCategory;

    @Column(nullable = false)
    private Long discount;

    @Column(nullable = false)
    private LocalTime createTime;

    @Column(nullable = false)
    private LocalTime updateTime;

    @Column(nullable = false)
    private Long productQuantity;

    @Column()
    private Float predictReviewStar;

    @Column()
    private Float productStar;

    // Product 수정 메서드
    public void updateProduct(String productName, String productExplanation, String productImage, Long productPrice,
                              Long productInventory, Category category,
                              Long discount, Long productQuantity, LocalTime updateTime) {
        this.productName = productName;
        this.productExplanation = productExplanation;
        this.productImage = productImage;
        this.productPrice = productPrice;
        this.productInventory = productInventory;
        this.category = category;
        this.discount = discount;
        this.productQuantity = productQuantity;
        this.updateTime = updateTime;
    }

    // Product 생성 메서드
    public static Product createProduct(Seller seller, String productName, String productExplanation,
                                        String productImage, Long productPrice, Long productInventory,
                                        Category category, DetailCategory detailCategory, Long discount,
                                        Long productQuantity) {
        return Product.builder()
                .seller(seller)
                .productName(productName)
                .productExplanation(productExplanation)
                .productImage(productImage)
                .productPrice(productPrice)
                .productInventory(productInventory)
                .category(category)
                .discount(discount)
                .productQuantity(productQuantity)
                .createTime(LocalTime.now())
                .updateTime(LocalTime.now())
                .predictReviewStar(0.0f)  // 초기 예상 리뷰 별점
                .productStar(0.0f)        // 초기 실제 리뷰 별점
                .build();
    }
}
