package com.kakaoteck.golagola.domain.seller.controller;

import com.kakaoteck.golagola.domain.seller.dto.SellerRequest;
import com.kakaoteck.golagola.domain.seller.dto.SellerResponse;
import com.kakaoteck.golagola.domain.seller.entity.Seller;
import com.kakaoteck.golagola.domain.seller.repository.SellerRepository;
import com.kakaoteck.golagola.domain.seller.service.SellerService;
import com.kakaoteck.golagola.global.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/seller")
@CrossOrigin("*")
public class SellerController {

    private final SellerService sellerService;
    private final SellerRepository sellerRepository;

    @Operation(summary = "판매자 마이페이지 조회", description = "판매자의 정보를 조회합니다.")
    @GetMapping("/mypage")
    public ApiResponse<SellerResponse> getMyPage(@AuthenticationPrincipal Seller seller) {
        return ApiResponse.onSuccess(sellerService.getMyPage(seller));
    }

    @Operation(summary = "판매자 마이페이지 수정", description = "판매자의 정보를 수정합니다.")
    @PutMapping("/mypage")
    public ApiResponse<SellerResponse> updateProfile(
            @AuthenticationPrincipal Seller seller,
            @RequestBody SellerRequest.MyPagePutDto request
            ) {
        return ApiResponse.onSuccess(sellerService.updateMyPage(seller, request));
    }
}
