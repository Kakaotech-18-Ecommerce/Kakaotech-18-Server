package com.kakaoteck.golagola.domain.order.service;

import com.kakaoteck.golagola.domain.buyer.entity.Buyer;
import com.kakaoteck.golagola.domain.cart.entity.Cart;
import com.kakaoteck.golagola.domain.cart.entity.CartProduct;
import com.kakaoteck.golagola.domain.cart.repository.CartProductRepository;
import com.kakaoteck.golagola.domain.order.dto.OrderResponse;
import com.kakaoteck.golagola.domain.order.entity.Order;
import com.kakaoteck.golagola.domain.order.repository.OrderRepository;
import com.kakaoteck.golagola.domain.orderProduct.entity.OrderProduct;
import com.kakaoteck.golagola.domain.orderProduct.repository.OrderProductRepository;
import com.kakaoteck.golagola.domain.product.entity.Product;
import com.kakaoteck.golagola.domain.seller.entity.Seller;
import com.kakaoteck.golagola.global.common.code.status.ErrorStatus;
import com.kakaoteck.golagola.global.common.enums.OrderStatus;
import com.kakaoteck.golagola.global.common.exception.GeneralException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderProductRepository orderProductRepository;
    private final CartProductRepository cartProductRepository;

    public List<OrderResponse> makeOrder(Buyer buyer) {
        Cart cart = buyer.getCart();

        if (cart.getCartProducts().isEmpty()) {
            throw new GeneralException(ErrorStatus._CART_IS_ALREADY_EMPTY);
        }

        // 판매자별로 주문을 생성할 수 있도록 그룹화
        Map<Seller, List<CartProduct>> sellerCartProductMap = cart.getCartProducts().stream()
                .collect(Collectors.groupingBy(cartProduct -> cartProduct.getProduct().getSeller()));

        List<OrderResponse> orderResponses = new ArrayList<>();

        for (Map.Entry<Seller, List<CartProduct>> entry : sellerCartProductMap.entrySet()) {
            Seller seller = entry.getKey();
            List<CartProduct> sellerCartProducts = entry.getValue();

            // 새로운 Order 생성
            Order order = Order.builder()
                    .buyer(buyer)
                    .seller(seller)  // 각 판매자에 대한 Order 생성
                    .orderDate(LocalDate.now())
                    .orderStatus(OrderStatus.READY)
                    .orderAddress(buyer.getAddress())
                    .orderExpectation(LocalDate.now().plusDays(3)) // 예상 도착일 설정
                    .orderArrival(LocalDate.now().plusDays(7)) // 실제 도착일 설정
                    .paymentDate(LocalDate.now()) // 결제 날짜 설정 (필요에 따라)
                    .isPay(true)
                    .build();

            orderRepository.save(order);

            // CartProduct를 OrderProduct로 변환하고 저장
            List<OrderProduct> orderProducts = sellerCartProducts.stream()
                    .map(cartProduct -> {
                        Product product = cartProduct.getProduct();
                        OrderProduct orderProduct = OrderProduct.builder()
                                .product(product)
                                .order(order)
                                .quantity(cartProduct.getQuantity()) // 수량은 CartProduct의 수량 사용
                                .orderPrice((product.getProductPrice() * cartProduct.getQuantity()) / product.getDiscount())
                                .build();
                        orderProductRepository.save(orderProduct);
                        return orderProduct;
                    })
                    .collect(Collectors.toList());

            // Cart에서 주문된 제품 제거
            cartProductRepository.deleteAll(sellerCartProducts);

            // 주문 결과 생성
            OrderResponse orderResponse = createOrderResponse(order, orderProducts);
            orderResponses.add(orderResponse);
        }

        return orderResponses;
    }

    private OrderResponse createOrderResponse(Order order, List<OrderProduct> orderProducts) {
        // OrderProductInfo 리스트 생성
        List<OrderResponse.OrderProductInfo> orderProductInfos = orderProducts.stream()
                .map(orderProduct -> OrderResponse.OrderProductInfo.builder()
                        .productName(orderProduct.getProduct().getProductName())
                        .quantity(orderProduct.getQuantity())
                        .price(orderProduct.getOrderPrice())
                        .totalPrice(orderProduct.getOrderPrice() * orderProduct.getQuantity())
                        .build())
                .collect(Collectors.toList());

        // 총 금액 계산
        Long totalAmount = orderProductInfos.stream()
                .mapToLong(OrderResponse.OrderProductInfo::totalPrice)
                .sum();

        // OrderResponse 구성 및 반환
        return OrderResponse.builder()
                .orderId(order.getOrderId())
                .buyerName(order.getBuyer().getRealName())
                .orderDate(order.getOrderDate())
                .products(orderProductInfos)
                .totalAmount(totalAmount)
                .isPaid(order.isPay())
                .expectedArrivalDate(order.getOrderExpectation())
                .build();
    }
}