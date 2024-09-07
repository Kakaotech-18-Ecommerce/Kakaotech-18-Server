package com.kakaoteck.golagola.domain.buyer.dto;

import com.kakaoteck.golagola.global.common.enums.Gender;
import com.kakaoteck.golagola.global.common.enums.Role;
import lombok.Builder;

import java.time.LocalDate;

public record BuyerRequest(
        String nickname,
        String realName,
        Gender gender,
        String email,
        String address,
        String phoneNum,
        Role role)
{

    @Builder
    public record MyPagePutDto(
            String nickname,
            String address,
            String phoneNum
    ) {}
}
