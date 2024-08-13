package com.kakaoteck.golagola.domain.product.controller;

import com.kakaoteck.golagola.domain.product.dto.ProductRequest;
import com.kakaoteck.golagola.domain.product.service.ProductService;
import com.kakaoteck.golagola.domain.seller.dto.SellerResponse;
import com.kakaoteck.golagola.domain.seller.entity.Seller;
import com.kakaoteck.golagola.global.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/product")
@CrossOrigin("*")
public class ProductController {

    private final ProductService productService;

    @Operation(summary = "상품 등록", description = "상품을 등록합니다. 판매자 전용")
    @PostMapping()
    public ApiResponse<String> postProduct(
            @AuthenticationPrincipal Seller seller,
            @RequestBody ProductRequest request
    ) {
        return ApiResponse.onSuccess(productService.postProduct(seller, request));
    }
}
