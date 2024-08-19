package com.kakaoteck.golagola.domain.cart.service;

import com.kakaoteck.golagola.domain.buyer.entity.Buyer;
import com.kakaoteck.golagola.domain.cart.dto.CartResponse;
import com.kakaoteck.golagola.domain.cart.entity.Cart;
import com.kakaoteck.golagola.domain.cart.entity.CartProduct;
import com.kakaoteck.golagola.domain.cart.repository.CartProductRepository;
import com.kakaoteck.golagola.domain.product.dto.ProductResponse;
import com.kakaoteck.golagola.domain.product.entity.Product;
import com.kakaoteck.golagola.domain.product.repository.ProductRepository;
import com.kakaoteck.golagola.global.common.code.status.ErrorStatus;
import com.kakaoteck.golagola.global.common.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CartService {

    private final ProductRepository productRepository;
    private final CartProductRepository cartProductRepository;

    public CartResponse getCartProducts(Buyer buyer) {
        Cart cart = buyer.getCart();

        // Lazy 로딩을 명시적으로 초기화
        Hibernate.initialize(cart.getCartProducts());

        // CartProduct 객체에서 Product를 추출하여 리스트 생성
        List<ProductResponse.ProductDto> products = cart.getCartProducts().stream()
                .map(cartProduct -> {
                    Product product = cartProduct.getProduct();
                    return ProductResponse.ProductDto.builder()
                            .productId(product.getProductId())
                            .productName(product.getProductName())
                            .productExplanation(product.getProductExplanation())
                            .productPrice(product.getProductPrice())
                            .build();
                })
                .collect(Collectors.toList());

        return CartResponse.builder()
                .productList(products)
                .build();
    }

    public CartResponse addCartProduct(Buyer buyer, Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new GeneralException(ErrorStatus._NOT_FOUND_PRODUCT));

        Cart cart = buyer.getOrCreateCart();

        // 장바구니에 이미 동일한 상품이 있는지 확인
        boolean productExistsInCart = cart.getCartProducts().stream()
                .anyMatch(cp -> cp.getProduct().getProductId().equals(productId));

        if (productExistsInCart) {
            throw new GeneralException(ErrorStatus._PRODUCT_ALREADY_IN_CART);  // 이미 존재한다면 예외를 던집니다.
        }

        // 장바구니에 상품 추가
        CartProduct cartProduct = CartProduct.builder()
                .cart(cart)
                .product(product)
                .build();

        cartProductRepository.save(cartProduct);

        return getCartProducts(buyer);
    }

    public CartResponse deleteCartProduct(Buyer buyer, Long productId) {
        Cart cart = buyer.getCart();

        CartProduct cartProduct = cart.getCartProducts().stream()
                .filter(cp -> cp.getProduct().getProductId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new GeneralException(ErrorStatus._NOT_FOUND_PRODUCT_IN_CART));

        cartProductRepository.delete(cartProduct);

        return getCartProducts(buyer);
    }

    public String emptyCart(Buyer buyer) {
        Cart cart = buyer.getCart();

        if (cart.getCartProducts().isEmpty()) {
            throw new GeneralException(ErrorStatus._CART_IS_ALREADY_EMPTY);
        }

        cartProductRepository.deleteAll(cart.getCartProducts());

        return "장바구니에 있는 모든 제품이 삭제되었습니다.";
    }
}
