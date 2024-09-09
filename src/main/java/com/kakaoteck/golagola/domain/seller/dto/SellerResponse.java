package com.kakaoteck.golagola.domain.seller.dto;

import com.kakaoteck.golagola.global.common.enums.Gender;
import com.kakaoteck.golagola.global.common.enums.Role;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record SellerResponse(
        String nickname,
        String realName,
        Gender gender,
        String email,
        String roadAddress, // 도로명 주소
        String zipCode, // 우편 번호
        String detailAdress, // 세부주소
        String phoneNum,
        Role role,
        LocalDate registerDate
) {
}
