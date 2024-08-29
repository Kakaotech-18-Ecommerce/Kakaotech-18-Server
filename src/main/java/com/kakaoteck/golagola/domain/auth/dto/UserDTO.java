package com.kakaoteck.golagola.domain.auth.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {

    private Long id; // 엔티티의 id 추가
    private String role;
    private String name;
    private String username;
    private String email; // 엔티티의 email 추가
    private String refreshToken; // 엔티티의 refreshToken 추가
    private boolean loginStatus; // 엔티티의 loginStatus 추가
}
