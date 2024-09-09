package com.kakaoteck.golagola.domain.auth.dto;

import com.kakaoteck.golagola.global.common.enums.Gender;
import com.kakaoteck.golagola.global.common.enums.Role;

public record AuthRequest(
        String nickName,
        Gender gender,
        String phoneNumber,
        String roadAddress,
        String zipCode,
        String detailAdress,
        Role role
) {
}
