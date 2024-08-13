package com.kakaoteck.golagola.domain.order.controller;

import com.kakaoteck.golagola.domain.buyer.entity.Buyer;
import com.kakaoteck.golagola.domain.order.dto.OrderResponse;

import com.kakaoteck.golagola.domain.order.service.OrderService;
import com.kakaoteck.golagola.global.common.ApiResponse;
import io.lettuce.core.StrAlgoArgs;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/order")
@CrossOrigin("*")
public class OrderController {

    private final OrderService orderService;

    @Operation(summary = "상품 주문", description = "장바구니에 담긴 상품을 주문합니다.")
    @PostMapping()
    public ApiResponse<List<OrderResponse>> makeOrder(
            @AuthenticationPrincipal Buyer buyer
    ) {
        return ApiResponse.onSuccess(orderService.makeOrder(buyer));
    }

}
