package com.kakaoteck.golagola.domain.cart.entity;

import com.kakaoteck.golagola.domain.buyer.entity.Buyer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Table(name = "cart_table")
public class Cart {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_id")
    private Buyer buyer;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartId;

}
