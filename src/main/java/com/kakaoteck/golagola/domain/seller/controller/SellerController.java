package com.kakaoteck.golagola.domain.seller.controller;

import com.kakaoteck.golagola.domain.seller.dto.SellerResponse;
import com.kakaoteck.golagola.domain.seller.entity.Seller;
import com.kakaoteck.golagola.domain.seller.service.SellerService;
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
@RequestMapping("/api/v1/seller")
@CrossOrigin("*")
public class SellerController {

    private final SellerService sellerService;

    @Operation(summary = "판매자 마이페이지 조회", description = "판매자의 정보를 조회합니다.")
    @GetMapping("/mypage")
    public ApiResponse<SellerResponse> getMyPage(
            @AuthenticationPrincipal Seller seller
            ) {
        return ApiResponse.onSuccess(sellerService.getMyPage(seller));
    }
}
