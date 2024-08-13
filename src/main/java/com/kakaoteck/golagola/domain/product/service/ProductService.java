package com.kakaoteck.golagola.domain.product.service;

import com.kakaoteck.golagola.domain.product.dto.ProductRequest;
import com.kakaoteck.golagola.domain.product.entity.Product;
import com.kakaoteck.golagola.domain.product.repository.ProductRepository;
import com.kakaoteck.golagola.domain.seller.entity.Seller;
import com.kakaoteck.golagola.domain.seller.repository.SellerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ProductService {

    private final SellerRepository sellerRepository;
    private final ProductRepository productRepository;

    public String postProduct(Seller seller, ProductRequest request) {
        // Product 객체 생성
        Product product = Product.builder()
                .seller(seller)
                .productName(request.productName())
                .productExplanation(request.productExplanation())
                .productImage(request.productImage())
                .productPrice(request.productPrice())
                .productInventory(request.productInventory())
                .category(request.category())
                .detailCategory(request.detailCategory())
                .discount(request.discount())
                .createTime(LocalTime.now())
                .updateTime(LocalTime.now())
                .productQuantity(request.productQuantity())
                .predictReviewStar(0.0f)  // 초기 예상 리뷰 별점
                .productStar(0.0f)         // 초기 실제 리뷰 별점
                .build();

        // Product 저장
        productRepository.save(product);

        return "상품등록 성공";
    }
}
