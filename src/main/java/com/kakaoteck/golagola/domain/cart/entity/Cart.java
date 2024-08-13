package com.kakaoteck.golagola.domain.cart.entity;

import com.kakaoteck.golagola.domain.buyer.entity.Buyer;
import com.kakaoteck.golagola.domain.product.entity.Product;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<CartProduct> cartProducts = new ArrayList<>();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartId;

    public void assignBuyer(Buyer buyer) {
        this.buyer = buyer;
        if (buyer.getCart() != this) {
            buyer.assignCart(this);
        }
    }

    public void addProduct(Product product) {
        CartProduct cartProduct = new CartProduct();
        cartProduct.assignCart(this);
        cartProduct.assignProduct(product);
        this.cartProducts.add(cartProduct);
    }
}
