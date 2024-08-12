package com.kakaoteck.golagola.domain.order.entity;

import com.kakaoteck.golagola.domain.buyer.entity.Buyer;
import com.kakaoteck.golagola.domain.orderProduct.entity.OrderProduct;
import com.kakaoteck.golagola.domain.seller.entity.Seller;
import com.kakaoteck.golagola.global.common.BaseEntity;
import com.kakaoteck.golagola.global.common.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Table(name = "order_table") // order가 mySQL 예약어임 이슈 !
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id", nullable = false)
    private Seller seller;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_id", nullable = false)
    private Buyer buyer;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderProduct> orderProductList;

    @Column(nullable = false)
    private boolean isPay;

    @Column(nullable = false)
    private LocalDate paymentDate;

    @Column(nullable = false)
    private String orderAddress;

    @Column(nullable = true)
    private String orderNotes;

    @Column(nullable = false)
    private LocalDate orderArrival;

    @Column(nullable = false)
    private LocalDate orderExpectation;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus orderStatus;

    @Column(nullable = false)
    private LocalDate orderDate;

    public static Order from(Long orderId, Seller seller, Buyer buyer, List<OrderProduct> orderProductList,
                             boolean isPay, LocalDate paymentDate, String orderAddress,
                             String orderNotes, LocalDate orderArrival, LocalDate orderExpectation,
                             OrderStatus orderStatus, LocalDate orderDate) {
        return Order.builder()
                .orderId(orderId)
                .seller(seller)
                .buyer(buyer)
                .orderProductList(orderProductList)
                .isPay(isPay)
                .paymentDate(paymentDate)
                .orderAddress(orderAddress)
                .orderNotes(orderNotes)
                .orderArrival(orderArrival)
                .orderExpectation(orderExpectation)
                .orderStatus(orderStatus)
                .orderDate(orderDate)
                .build();
    }
}
