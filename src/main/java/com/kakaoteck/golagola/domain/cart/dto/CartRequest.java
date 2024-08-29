package com.kakaoteck.golagola.domain.cart.dto;

import com.kakaoteck.golagola.domain.product.entity.Product;
import lombok.Builder;

@Builder
public record CartRequest(
        Long productId,
        Long quantity

) {
}
