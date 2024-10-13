package com.pickple.commerceservice.application.service;

import com.pickple.commerceservice.application.dto.OrderCreateResponseDto;
import com.pickple.commerceservice.application.dto.OrderDetailResponseDto;
import com.pickple.commerceservice.application.dto.OrderResponseDto;
import com.pickple.commerceservice.domain.model.Order;
import com.pickple.commerceservice.domain.model.OrderDetail;
import com.pickple.commerceservice.domain.model.OrderStatus;
import com.pickple.commerceservice.domain.model.Product;
import com.pickple.commerceservice.domain.repository.OrderRepository;
import com.pickple.commerceservice.domain.repository.ProductRepository;
import com.pickple.commerceservice.exception.CommerceErrorCode;
import com.pickple.commerceservice.infrastructure.feign.DeliveryClient;
import com.pickple.commerceservice.infrastructure.feign.PaymentClient;
import com.pickple.commerceservice.infrastructure.feign.dto.PaymentClientDto;
import com.pickple.commerceservice.infrastructure.messaging.OrderMessagingProducerService;
import com.pickple.commerceservice.presentation.dto.request.OrderCreateRequestDto;
import com.pickple.common_module.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final TemporaryStorageService temporaryStorageService;
    private final OrderMessagingProducerService messagingProducerService;
    private final ProductRepository productRepository;
    private final PaymentClient paymentClient;
//    private final DeliveryClient deliveryClient;

    /**
     * 주문 생성
     */
    @Transactional
    public OrderCreateResponseDto createOrder(OrderCreateRequestDto requestDto, String username) {
        // 주문 정보 생성
        Order order = Order.builder()
                .orderStatus(OrderStatus.PENDING)
                .username(username)
                .build();

        // OrderDetail 정보 생성
        List<OrderDetail> orderDetails = requestDto.getOrderDetails().stream()
                .map(detail -> {
                    Product product = productRepository.findById(detail.getProductId())
                            .orElseThrow(() -> new CustomException(CommerceErrorCode.PRODUCT_NOT_FOUND));
                    BigDecimal unitPrice = product.getProductPrice();
                    OrderDetail orderDetail = OrderDetail.builder()
                            .order(order)
                            .product(product)
                            .orderQuantity(detail.getOrderQuantity())
                            .unitPrice(unitPrice)
                            .build();
                    orderDetail.calculateTotalPrice(); // 단가*수량 계산
                    return orderDetail;
                })
                .collect(Collectors.toList());

        order.addOrderDetails(orderDetails);
        order.calculateTotalAmount();

        orderRepository.save(order);

        // 결제 요청 (kafka)
        messagingProducerService.sendPaymentRequest(
                order.getOrderId(),
                order.getAmount(),
                username
        );

        // 배송 정보 저장 (redis)
        temporaryStorageService.storeDeliveryInfo(order.getOrderId(), requestDto.getDeliveryInfo());

        // OrderDetail Dto로 변환
        List<OrderDetailResponseDto> orderDetailDtos = order.getOrderDetails().stream()
                .map(detail -> OrderDetailResponseDto.builder()
                        .productId(detail.getProduct().getProductId())
                        .orderQuantity(detail.getOrderQuantity())
                        .totalPrice(detail.getTotalPrice())
                        .build())
                .collect(Collectors.toList());

        return OrderCreateResponseDto.builder()
                .orderId(order.getOrderId())
                .username(username)
                .amount(order.getAmount())
                .orderStatus(order.getOrderStatus().name())
                .orderDetails(orderDetailDtos)
                .build();
    }

    @Transactional(readOnly = true)
    public OrderResponseDto getOrderById(UUID orderId, String role, String username) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(CommerceErrorCode.ORDER_NOT_FOUND));

        PaymentClientDto paymentInfo = paymentClient.getPaymentInfo(role, username, orderId);
//        DeliveryClient deliveryClient = deliveryClient.getDeliveryInfo(role, username, orderId);

        List<OrderDetailResponseDto> orderDetailDtos = order.getOrderDetails().stream()
                .map(detail -> OrderDetailResponseDto.builder()
                        .productId(detail.getProduct().getProductId())
                        .orderQuantity(detail.getOrderQuantity())
                        .totalPrice(detail.getTotalPrice())
                        .build())
                .collect(Collectors.toList());

        return OrderResponseDto.builder()
                .orderId(order.getOrderId())
                .username(order.getUsername())
                .amount(order.getAmount())
                .orderStatus(order.getOrderStatus().name())
                .orderDetails(orderDetailDtos)
                .paymentInfo(paymentInfo)
//                .deliveryInfo(deliveryInfo)
                .build();
    }
}