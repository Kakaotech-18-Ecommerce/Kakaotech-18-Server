package com.kakaoteck.golagola.domain.auth.dto;

import com.kakaoteck.golagola.domain.buyer.entity.Buyer;
import com.kakaoteck.golagola.domain.seller.entity.Seller;
import com.kakaoteck.golagola.global.common.enums.Gender;
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
    private String nickname;
    private String image;
    private Gender gender;
    private Buyer buyer;
    private Seller seller;
//    private String refreshToken; // 엔티티의 refreshToken 추가
//    private boolean loginStatus; // 엔티티의 loginStatus 추가
}
