package com.kakaoteck.golagola.domain.order.entity;

import com.kakaoteck.golagola.global.common.BaseEntity;
import com.kakaoteck.golagola.global.common.enums.OrderStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    private boolean isPay;
    private LocalDate paymentDate;
    private String orderAddress;
    private String orderNotes;
    private LocalDate orderArrival;
    private LocalDate orderExpectation;
    private OrderStatus orderStatus;
    private LocalDate orderDate;
}
