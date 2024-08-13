package com.kakaoteck.golagola.domain.order.dto;

import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Builder
public record OrderResponse(
        Long orderId,
        String buyerName,                // 구매자 이름
        String sellerName,               // 판매자 이름
        LocalDate orderDate,             // 주문 날짜
        List<OrderProductInfo> products, // 주문한 제품 정보
        Long totalAmount,                // 총 금액
        boolean isPaid,                  // 결제 여부
        LocalDate expectedArrivalDate    // 예상 도착 날짜
) {

    @Builder
    public static record OrderProductInfo(
            String productName,  // 제품 이름
            Long quantity,       // 수량
            Long price,          // 가격
            Long totalPrice      // 총 가격 (수량 * 가격)
    ) {}
}