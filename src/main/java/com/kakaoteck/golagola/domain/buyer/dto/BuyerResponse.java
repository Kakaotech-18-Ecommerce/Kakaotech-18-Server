package com.kakaoteck.golagola.domain.buyer.dto;

import com.kakaoteck.golagola.global.common.enums.Gender;
import com.kakaoteck.golagola.global.common.enums.Role;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record BuyerResponse(
        String nickname,
        String realName,
        Gender gender,
        String email,
        String address,
        String phoneNum,
        Role role,
        LocalDate registerDate
) {
}
