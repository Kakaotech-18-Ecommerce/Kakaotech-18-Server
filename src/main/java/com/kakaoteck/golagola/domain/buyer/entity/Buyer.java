package com.kakaoteck.golagola.domain.buyer.entity;

import com.kakaoteck.golagola.domain.buyer.dto.BuyerRequest;
import com.kakaoteck.golagola.domain.cart.entity.Cart;
import com.kakaoteck.golagola.domain.order.entity.Order;
import com.kakaoteck.golagola.domain.product.entity.Product;
import com.kakaoteck.golagola.domain.review.entity.Review;
import com.kakaoteck.golagola.global.common.enums.Gender;
import com.kakaoteck.golagola.global.common.enums.Role;
import com.kakaoteck.golagola.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Table(name = "buyer_table")
public class Buyer extends BaseEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long buyerId;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String realName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String phoneNum;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(nullable = false)
    private LocalDate registerDate;

    @OneToOne(mappedBy = "buyer", cascade = CascadeType.ALL)
    private Cart cart;

    @OneToMany(mappedBy = "buyer", cascade = CascadeType.ALL)
    private List<Review> reviewList;

    @OneToMany(mappedBy = "buyer", cascade = CascadeType.ALL)
    private List<Order> orderList;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void updateProfile(BuyerRequest.MyPagePutDto request) {
        this.nickname = request.nickname();
        this.address = request.address();
        this.phoneNum = request.phoneNum();
    }

    public void assignCart(Cart cart) {
        if (cart != null) {
            this.cart = cart;
            cart.assignBuyer(this);
        }
    }

    public void addProductToCart(Product product) {
        if (this.cart == null) {
            this.cart = new Cart(); // 새로운 Cart 객체 생성
            this.cart.assignBuyer(this); // Cart와 Buyer 간의 양방향 연관관계 설정
        }
        this.cart.getProductList().add(product);
    }

    public Cart getOrCreateCart() {
        if (this.cart == null) {
            this.cart = new Cart(); // 새로운 Cart 객체 생성
            this.cart.assignBuyer(this); // Cart와 Buyer 간의 양방향 연관관계 설정
        }
        return this.cart;
    }

    public static Buyer from(Long buyerId, String nickname, String realName, Gender gender, String email, String password,
                             String address, String phoneNum, Role role, LocalDate registerDate) {
        return Buyer.builder()
                .buyerId(buyerId)
                .nickname(nickname)
                .realName(realName)
                .gender(gender)
                .email(email)
                .password(password)
                .address(address)
                .phoneNum(phoneNum)
                .role(role)
                .registerDate(registerDate)
                .build();
    }
}

