package com.kakaoteck.golagola.domain.product.controller;

import com.kakaoteck.golagola.domain.product.dto.ProductRequest;
import com.kakaoteck.golagola.domain.product.dto.ProductResponse;
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

    @Operation(summary = "상품 수정", description = "상품 정보를 수정합니다.")
    @PutMapping("/{productId}")
    public ApiResponse<ProductResponse> modifyProduct(
            @AuthenticationPrincipal Seller seller,
            @PathVariable Long productId,
            @RequestBody ProductRequest request
    ) {
        return ApiResponse.onSuccess(productService.modifyProduct(seller, productId, request));
    }

    @Operation(summary = "상품 삭제", description = "상품을 삭제합니다. 판매자 전용")
    @DeleteMapping("/{productId}")
    public ApiResponse<String> deleteProduct(
            @AuthenticationPrincipal Seller seller,
            @PathVariable Long productId
    ) {
        productService.deleteProduct(seller, productId);
        return ApiResponse.onSuccess("상품이 성공적으로 삭제되었습니다.");
    }

}
