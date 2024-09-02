package com.kakaoteck.golagola.domain.buyer.entity;

import com.kakaoteck.golagola.domain.auth.entity.UserEntity;
import com.kakaoteck.golagola.domain.buyer.dto.BuyerRequest;
import com.kakaoteck.golagola.domain.cart.entity.Cart;
import com.kakaoteck.golagola.domain.cart.entity.CartProduct;
import com.kakaoteck.golagola.domain.order.entity.Order;
import com.kakaoteck.golagola.domain.product.entity.Product;
import com.kakaoteck.golagola.domain.review.entity.Review;
import com.kakaoteck.golagola.global.common.enums.Gender;
import com.kakaoteck.golagola.global.common.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@SuperBuilder
//@Table(name = "buyer_table")
@DiscriminatorValue("BUYER")
public class Buyer extends UserEntity {

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long buyerId;


    private String address; // @Column(nullable = false)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role = Role.valueOf("SELLER");

    @OneToOne(mappedBy = "buyer", cascade = CascadeType.ALL, orphanRemoval = true)
    private Cart cart;

    @OneToMany(mappedBy = "buyer", cascade = CascadeType.ALL)
    private List<Review> reviewList;

    @OneToMany(mappedBy = "buyer", cascade = CascadeType.ALL)
    private List<Order> orderList;

    public void updateProfile(BuyerRequest.MyPagePutDto request) {
        this.address = request.address();
        this.setNickname(request.nickname()); // 상위클래스인 UserEntity에 업데이트
        this.setPhoneNum(request.phoneNum()); // 상위클래스인 UserEntity에 업데이트
    }

    public void assignCart(Cart cart) {
        this.cart = cart;
        if (cart.getBuyer() != this) {
            cart.assignBuyer(this);
        }
    }

    // Cart를 자동으로 초기화
    @PrePersist
    public void initializeCart() {
        if (this.cart == null) {
            Cart newCart = new Cart();
            newCart.assignBuyer(this);
            this.cart = newCart;
        }
    }

    public Cart getOrCreateCart() {
        if (this.cart == null) {
            Cart newCart = new Cart();
            newCart.assignBuyer(this);
            this.cart = newCart;
        }
        return this.cart;
    }

    public void addProductToCart(Product product) {
        Cart cart = this.getOrCreateCart();
        cart.addProduct(product);
    }

//    @Builder
//    public Buyer(Long id, String nickname, String realName, Gender gender, String email,
//                 String address, String phoneNum, Role role) {
//        this.setId(id); // UserEntity의 필드 설정
//        this.setNickname(nickname); // UserEntity의 필드 설정
//        this.setName(realName); // UserEntity의 필드 설정
//        this.setGender(gender); // UserEntity의 필드 설정
//        this.setEmail(email); // UserEntity의 필드 설정
//        this.setPhoneNum(phoneNum); // UserEntity의 필드 설정
//        this.setRole(role); // UserEntity의 필드 설정
//
//        this.address = address; // Buyer 클래스의 필드 설정
//    }
//
//    public static Buyer from(Long buyerId, String nickname, String realName, Gender gender, String email, String password,
//                             String address, String phoneNum, Role role) {
//        return Buyer.builder()
//                .buyerId(buyerId)
//                .nickname(nickname)
//                .realName(realName)
//                .gender(gender)
//                .email(email)
//                .password(password)
//                .address(address)
//                .phoneNum(phoneNum)
//                .role(role)
//                .build();
//    }
//
}
