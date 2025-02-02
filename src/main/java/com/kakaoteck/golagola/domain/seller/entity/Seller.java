package com.kakaoteck.golagola.domain.seller.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.ser.Serializers;
import com.kakaoteck.golagola.domain.auth.entity.UserEntity;
import com.kakaoteck.golagola.domain.buyer.dto.BuyerRequest;
import com.kakaoteck.golagola.domain.order.entity.Order;
import com.kakaoteck.golagola.domain.product.entity.Product;
import com.kakaoteck.golagola.domain.seller.dto.SellerRequest;
import com.kakaoteck.golagola.global.common.BaseEntity;
import com.kakaoteck.golagola.global.common.enums.Gender;
import com.kakaoteck.golagola.global.common.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Table(name = "seller_table")
public class Seller extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sellerId;
    private String roadAddress; // 도로명 주소
    private String zipCode; // 우편 번호
    private String detailAdress; // 세부주소

    @OneToOne // Seller는 하나의 UserEntity와만 연결됩니다.
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @OneToMany(mappedBy = "seller", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Product> productList = new ArrayList<>();

    @OneToMany(mappedBy = "seller", cascade = CascadeType.ALL)
    private List<Order> orderList = new ArrayList<>();

    public void updateProfile(SellerRequest.MyPagePutDto request) {
        this.roadAddress = request.roadAddress();
        this.zipCode = request.zipCode();
        this.detailAdress = request.detailAdress();
        this.user.setNickname(request.nickname()); // 참조entity에서 UserEntity에 업데이트
        this.user.setPhoneNum(request.phoneNum()); // 참조entity에서 UserEntity에 업데이트
    }

//    public static Seller from(Long sellerId, String nickname, Gender gender, String email, String password,
//                              String address, String phoneNum, Role role, String realName,
//                              LocalDate registerDate, List<Product> productList, List<Order> orderList) {
//        return Seller.builder()
//                .sellerId(sellerId)
//                .nickname(nickname)
//                .password(password)
//                .gender(gender)\
//                .email(email)
//                .address(address)
//                .phoneNum(phoneNum)
//                .role(role)
//                .realName(realName)
//                .registerDate(registerDate)
//                .productList(productList)
//                .orderList(orderList)
//                .build();
//    }
}
