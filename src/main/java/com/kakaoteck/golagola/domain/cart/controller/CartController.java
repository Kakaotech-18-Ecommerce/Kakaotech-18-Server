package com.kakaoteck.golagola.domain.cart.controller;

import com.kakaoteck.golagola.domain.buyer.entity.Buyer;
import com.kakaoteck.golagola.domain.cart.dto.CartResponse;
import com.kakaoteck.golagola.domain.cart.service.CartService;
import com.kakaoteck.golagola.global.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/cart")
@CrossOrigin("*")
public class CartController {

    private final CartService cartService;

    @Operation(summary = "상품 장바구니 조회", description = "장바구니에 담긴 상품을 조회합니다. / 목록으로 조회됨")
    @GetMapping()
    public ApiResponse<CartResponse> getCartProducts(@AuthenticationPrincipal Buyer buyer) {
        return ApiResponse.onSuccess(cartService.getCartProducts(buyer));
    }
}
