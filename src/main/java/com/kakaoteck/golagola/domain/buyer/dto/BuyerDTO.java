package com.kakaoteck.golagola.domain.buyer.dto;

import com.kakaoteck.golagola.global.common.enums.Gender;
import com.kakaoteck.golagola.global.common.enums.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BuyerDTO {
    private Long buyerId;     // Buyer의 ID
    private String nickname;  // UserEntity에서 가져온 필드
    private String realName;  // UserEntity에서 가져온 필드
    private String email;     // UserEntity에서 가져온 필드
    private String phoneNum;  // UserEntity에서 가져온 필드
    private Gender gender;    // UserEntity에서 가져온 필드
    private Role role;        // Buyer의 역할 (BUYER)
    private String address;   // Buyer의 주소
}
