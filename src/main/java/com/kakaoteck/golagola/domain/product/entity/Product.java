package com.kakaoteck.golagola.domain.product.entity;

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

    private String productName;
    private String productExplanation;
    private String productImage;
    private Long productPrice;
    private Long productInventory;
    private Category category;
    private DetailCategory detailCategory;
    private Long discount;
    private LocalTime createTime;
    private LocalTime updateTime;
    private Long productQuantity;
    private Float predictReviewStar;
    private Float productStar;

}
