package com.kakaoteck.golagola.domain.buyer.dto;

import com.kakaoteck.golagola.global.common.enums.Gender;
import com.kakaoteck.golagola.global.common.enums.Role;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record BuyerResponse(
        String email,
        Role role,
        String address,
        String realName,
        Gender gender,
        String phoneNum,
        String nickname
) {
}
