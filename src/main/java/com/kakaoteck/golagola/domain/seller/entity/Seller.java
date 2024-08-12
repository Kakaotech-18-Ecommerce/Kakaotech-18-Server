package com.kakaoteck.golagola.domain.seller.entity;

import com.fasterxml.jackson.databind.ser.Serializers;
import com.kakaoteck.golagola.domain.order.entity.Order;
import com.kakaoteck.golagola.domain.product.entity.Product;
import com.kakaoteck.golagola.global.common.BaseEntity;
import com.kakaoteck.golagola.global.common.enums.Gender;
import com.kakaoteck.golagola.global.common.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Table(name = "seller_table")
public class Seller extends BaseEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sellerId;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String phoneNum;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(nullable = false)
    private String realName;

    @Column(nullable = false)
    private LocalDate registerDate;

    @OneToMany(mappedBy = "seller", cascade = CascadeType.ALL)
    private List<Product> productList = new ArrayList<>();

    @OneToMany(mappedBy = "seller", cascade = CascadeType.ALL)
    private List<Order> orderList = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }

    public static Seller from(Long sellerId, String nickname, Gender gender, String email,
                              String address, String phoneNum, Role role, String realName,
                              LocalDate registerDate, List<Product> productList, List<Order> orderList) {
        return Seller.builder()
                .sellerId(sellerId)
                .nickname(nickname)
                .gender(gender)
                .email(email)
                .address(address)
                .phoneNum(phoneNum)
                .role(role)
                .realName(realName)
                .registerDate(registerDate)
                .productList(productList)
                .orderList(orderList)
                .build();
    }
}
