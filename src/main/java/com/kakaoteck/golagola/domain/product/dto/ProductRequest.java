package com.kakaoteck.golagola.domain.product.dto;

import com.kakaoteck.golagola.global.common.enums.Category;
import com.kakaoteck.golagola.global.common.enums.DetailCategory;
import lombok.Builder;

@Builder
public record ProductRequest(
        String productName,
        String productExplanation,
        String productImage,
        Long productPrice,
        Long productInventory,
        Category category,
        DetailCategory detailCategory,
        Long discount,
        Long productQuantity
) {
}
