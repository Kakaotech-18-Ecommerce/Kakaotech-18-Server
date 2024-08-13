package com.kakaoteck.golagola.domain.cart.controller;

import com.kakaoteck.golagola.domain.buyer.entity.Buyer;
import com.kakaoteck.golagola.domain.cart.dto.CartRequest;
import com.kakaoteck.golagola.domain.cart.dto.CartResponse;
import com.kakaoteck.golagola.domain.cart.service.CartService;
import com.kakaoteck.golagola.global.common.ApiResponse;
import io.lettuce.core.StrAlgoArgs;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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

    @Operation(summary = "상품 장바구니 추가", description = "장바구니에 상품을 추가합니다.")
    @PostMapping()
    public ApiResponse<CartResponse> addCartProduct(
            @AuthenticationPrincipal Buyer buyer,
            @RequestBody CartRequest request
    ) {
        return ApiResponse.onSuccess(cartService.addCartProduct(buyer, request.productId()));
    }

    @Operation(summary = "상품 장바구니 삭제", description = "장바구니에 있는 상품 목록을 수정합니다.")
    @DeleteMapping()
    public ApiResponse<CartResponse> deleteCartProduct(
            @AuthenticationPrincipal Buyer buyer,
            @RequestBody CartRequest request
    ) {
        return ApiResponse.onSuccess(cartService.deleteCartProduct(buyer, request.productId()));
    }

    @Operation(summary = "상품 장바구니 비우기", description = "장바구니를 비웁니다.")
    @DeleteMapping("/empty")
    public ApiResponse<String> emptyCart(
            @AuthenticationPrincipal Buyer buyer
    ) {
        return ApiResponse.onSuccess(cartService.emptyCart(buyer));
    }
}
