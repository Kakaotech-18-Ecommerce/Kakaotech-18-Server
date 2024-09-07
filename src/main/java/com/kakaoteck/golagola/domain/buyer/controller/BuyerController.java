package com.kakaoteck.golagola.domain.buyer.controller;

import com.kakaoteck.golagola.domain.auth.dto.CustomOAuth2User;
import com.kakaoteck.golagola.domain.buyer.dto.BuyerRequest;
import com.kakaoteck.golagola.domain.buyer.dto.BuyerResponse;
import com.kakaoteck.golagola.domain.buyer.entity.Buyer;
import com.kakaoteck.golagola.domain.buyer.service.BuyerService;
import com.kakaoteck.golagola.global.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLOutput;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/buyer")
public class BuyerController {

    private final BuyerService buyerService;

//    @Operation(summary = "구매자 마이페이지 조회", description = "구매자의 정보를 조회합니다.")
//    @GetMapping("/mypage")
//    public ApiResponse<BuyerResponse> getMyPage() {
//        // 1. jwt 세션 접근
//        CustomOAuth2User customUser = (CustomOAuth2User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        String username = customUser.getUsername();
//
//        return ApiResponse.onSuccess(BuyerService.getMyPage(username));
//    }

    @Operation(summary = "구매자 마이페이지 조회", description = "구매자의 정보를 조회합니다.")
    @GetMapping("/mypage")
    public String getMyPage(@AuthenticationPrincipal CustomOAuth2User customUser) {
        // 1. jwt 세션 접근
//        CustomOAuth2User customUser = (CustomOAuth2User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = customUser.getUsername();
        System.out.println("username: " + username);
//        return ApiResponse.onSuccess(BuyerService.getMyPage(username));
        return "good";
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
