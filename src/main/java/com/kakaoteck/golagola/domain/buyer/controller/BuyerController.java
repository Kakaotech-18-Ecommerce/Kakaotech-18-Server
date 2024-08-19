package com.kakaoteck.golagola.domain.buyer.controller;

import com.kakaoteck.golagola.domain.buyer.dto.BuyerRequest;
import com.kakaoteck.golagola.domain.buyer.dto.BuyerResponse;
import com.kakaoteck.golagola.domain.buyer.entity.Buyer;
import com.kakaoteck.golagola.domain.buyer.service.BuyerService;
import com.kakaoteck.golagola.global.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/buyer")
public class BuyerController {

    private final BuyerService buyerService;

    @Operation(summary = "구매자 마이페이지 조회", description = "구매자의 정보를 조회합니다.")
    @GetMapping("/mypage")
    public ApiResponse<BuyerResponse> getMyPage(
            @AuthenticationPrincipal Buyer buyer
            ) {
        return ApiResponse.onSuccess(BuyerService.getMyPage(buyer));
    }

    @Operation(summary = "구매자 마이페이지 수정", description = "구매자의 정보를 수정합니다.")
    @PutMapping("/mypage")
    public ApiResponse<BuyerResponse> updateProfile(
            @AuthenticationPrincipal Buyer buyer,
            @RequestBody BuyerRequest.MyPagePutDto request
            ) {
        return ApiResponse.onSuccess(buyerService.updateMyPage(buyer, request));
    }

}
