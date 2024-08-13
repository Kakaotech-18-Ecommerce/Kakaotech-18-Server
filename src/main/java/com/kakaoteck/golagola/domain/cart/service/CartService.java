package com.kakaoteck.golagola.domain.cart.service;

import com.kakaoteck.golagola.domain.buyer.dto.BuyerResponse;
import com.kakaoteck.golagola.domain.buyer.entity.Buyer;
import com.kakaoteck.golagola.domain.cart.dto.CartRequest;
import com.kakaoteck.golagola.domain.cart.dto.CartResponse;
import com.kakaoteck.golagola.domain.product.entity.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CartService {

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
}
