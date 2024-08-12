package com.kakaoteck.golagola.domain.auth.dto;

import com.kakaoteck.golagola.global.common.enums.Role;

import java.time.LocalDate;

public record JoinUserRequest(
        String email,
        String password,
        String nickname,
        String realName,
        String phoneNum,
        String address,
        String gender,
        String role
) {
}
