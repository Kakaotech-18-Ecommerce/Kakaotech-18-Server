package com.kakaoteck.golagola.domain.auth.dto;

public record AuthRequest(
        String email,
        String password
) {
}
