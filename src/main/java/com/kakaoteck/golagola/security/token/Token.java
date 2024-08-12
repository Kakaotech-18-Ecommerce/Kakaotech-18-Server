package com.kakaoteck.golagola.security.token;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kakaoteck.golagola.domain.buyer.entity.Buyer;
import com.kakaoteck.golagola.domain.seller.entity.Seller;
import com.kakaoteck.golagola.security.token.enums.TokenType;
import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "token")
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String token;

    @Enumerated(EnumType.STRING)
    private TokenType tokenType = TokenType.BEARER;

    private boolean revoked;

    private boolean expired;

    @JsonIgnore
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_id")
    private Buyer buyer;

    @JsonIgnore
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id")
    private Seller seller;

}
