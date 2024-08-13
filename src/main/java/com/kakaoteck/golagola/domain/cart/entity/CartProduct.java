package com.kakaoteck.golagola.domain.cart.entity;

import com.kakaoteck.golagola.domain.product.entity.Product;
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
@Table(name = "cart_product_table")
public class CartProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column
    private Long quantity;

    public void assignCart(Cart cart) {
        this.cart = cart;
        if (!cart.getCartProducts().contains(this)) {
            cart.getCartProducts().add(this);
        }
    }

    public void assignProduct(Product product) {
        this.product = product;
        if (!product.getCartProducts().contains(this)) {
            product.getCartProducts().add(this);
        }
    }
}
