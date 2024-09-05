package com.kakaoteck.golagola.domain.product.service;

import com.kakaoteck.golagola.domain.product.dto.ProductRequest;
import com.kakaoteck.golagola.domain.product.dto.ProductResponse;
import com.kakaoteck.golagola.domain.product.entity.Product;
import com.kakaoteck.golagola.domain.product.repository.ProductRepository;
import com.kakaoteck.golagola.domain.seller.entity.Seller;
import com.kakaoteck.golagola.domain.seller.repository.SellerRepository;
import com.kakaoteck.golagola.global.common.code.status.ErrorStatus;
import com.kakaoteck.golagola.global.common.exception.GeneralException;
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

    public ProductResponse modifyProduct(Seller seller, Long productId, ProductRequest request) {
        // productId로 해당 상품을 찾음
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new GeneralException(ErrorStatus._NOT_FOUND_PRODUCT));

        // 해당 상품이 현재 로그인한 seller가 등록한 것인지 확인
        if (!product.getSeller().getUser().getSeller().getSellerId().equals(seller.getSellerId())) {
            throw new GeneralException(ErrorStatus._UNAUTHORIZED_ACCESS);
        }

        // Product 정보 업데이트
        product.updateProduct(
                request.productName(),
                request.productExplanation(),
                request.productImage(),
                request.productPrice(),
                request.productInventory(),
                request.category(),
                request.detailCategory(),
                request.discount(),
                request.productQuantity(),
                LocalTime.now()  // updateTime을 현재 시간으로 업데이트
        );

        // 업데이트된 Product 저장
        productRepository.save(product);

        return ProductResponse.builder()
                .productId(product.getProductId())
                .productName(product.getProductName())
                .productExplanation(product.getProductExplanation())
                .productImage(product.getProductImage())
                .productPrice(product.getProductPrice())
                .productInventory(product.getProductInventory())
                .category(product.getCategory())
                .detailCategory(product.getDetailCategory())
                .discount(product.getDiscount())
                .productQuantity(product.getProductQuantity())
                .build();
    }

    public void deleteProduct(Seller seller, Long productId) {
        // productId로 Product를 찾음
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new GeneralException(ErrorStatus._NOT_FOUND_PRODUCT));

        // 해당 Product가 현재 로그인된 seller가 등록한 제품인지 확인
        if (!product.getSeller().getUser().getSeller().getSellerId().equals(seller.getSellerId())) {
            throw new GeneralException(ErrorStatus._UNAUTHORIZED_ACCESS);
        }

        // 해당 Product가 장바구니에 담겨 있는지 확인
        boolean isInCart = product.getCartProducts() != null && !product.getCartProducts().isEmpty();

        if (isInCart) {
            throw new GeneralException(ErrorStatus._PRODUCT_IN_CART_CANNOT_DELETE);
        }

        // Product 삭제
        productRepository.delete(product);
    }

}
