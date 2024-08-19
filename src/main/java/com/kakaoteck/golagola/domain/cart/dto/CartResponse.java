package com.kakaoteck.golagola.domain.cart.dto;

import com.kakaoteck.golagola.domain.product.dto.ProductResponse;
import com.kakaoteck.golagola.domain.product.entity.Product;
import lombok.Builder;

import java.util.List;

@Builder
public record CartResponse(
        List<ProductResponse.ProductDto> productList
) {
}
