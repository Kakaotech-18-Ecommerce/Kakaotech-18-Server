package com.kakaoteck.golagola.domain.cart.service;

import com.kakaoteck.golagola.domain.buyer.dto.BuyerResponse;
import com.kakaoteck.golagola.domain.buyer.entity.Buyer;
import com.kakaoteck.golagola.domain.cart.dto.CartRequest;
import com.kakaoteck.golagola.domain.cart.dto.CartResponse;
import com.kakaoteck.golagola.domain.product.entity.Product;
import com.kakaoteck.golagola.domain.product.repository.ProductRepository;
import com.kakaoteck.golagola.global.common.code.status.ErrorStatus;
import com.kakaoteck.golagola.global.common.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CartService {

    private final ProductRepository productRepository;

    public CartResponse getCartProducts(Buyer buyer) {
        // Cart가 null이 아닌 경우에만 productList를 가져오고, 그렇지 않으면 빈 리스트를 반환
        List<Product> productList = Collections.emptyList();
        if (buyer.getCart() != null) {
            productList = buyer.getCart().getProductList() != null ? buyer.getCart().getProductList() : Collections.emptyList();
        }

        return CartResponse.builder()
                .productList(productList)
                .build();
    }

    public CartResponse addCartProduct(Buyer buyer, Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new GeneralException(ErrorStatus._NOT_FOUND_PRODUCT));

        // Buyer의 cart에 product를 추가
        buyer.addProductToCart(product);

        return CartResponse.builder()
                .productList(buyer.getCart().getProductList())
                .build();
    }

    public CartResponse deleteCartProduct(Buyer buyer, Long productId) {
        if (buyer.getCart() == null || buyer.getCart().getProductList() == null) {
            throw new GeneralException(ErrorStatus._NOT_FOUND_PRODUCT_IN_CART);
        }

        // Buyer의 cart에서 해당 productId에 해당하는 제품을 찾음
        List<Product> productList = buyer.getCart().getProductList();
        Product productToRemove = productList.stream()
                .filter(product -> product.getProductId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new GeneralException(ErrorStatus._NOT_FOUND_PRODUCT_IN_CART));

        // 제품을 장바구니에서 삭제
        productList.remove(productToRemove);

        return CartResponse.builder()
                .productList(productList)
                .build();
    }
}
