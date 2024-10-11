package com.pickple.commerceservice.application.service;

import com.pickple.commerceservice.domain.model.Order;
import com.pickple.commerceservice.domain.model.OrderDetail;
import com.pickple.commerceservice.domain.model.OrderStatus;
import com.pickple.commerceservice.domain.model.Product;
import com.pickple.commerceservice.domain.repository.OrderRepository;
import com.pickple.commerceservice.domain.repository.ProductRepository;
import com.pickple.commerceservice.exception.CommerceErrorCode;
import com.pickple.commerceservice.presentation.dto.request.OrderCreateRequestDto;
import com.pickple.commerceservice.application.dto.OrderCreateResponseDto;
import com.pickple.commerceservice.infrastructure.messaging.OrderMessagingProducerService;
import com.pickple.commerceservice.application.dto.OrderDetailResponseDto;
import com.pickple.common_module.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final TemporaryStorageService temporaryStorageService;
    private final OrderMessagingProducerService messagingProducerService;
    private final ProductRepository productRepository;

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

        // OrderDetail 생성
        List<OrderDetail> orderDetails = requestDto.getOrderDetails().stream()
                .map(detail -> {
                    Product product = productRepository.findById(detail.getProductId())
                            .orElseThrow(() -> new CustomException(CommerceErrorCode.PRODUCT_NOT_FOUND));
                    return OrderDetail.builder()
                            .order(order)
                            .product(product)
                            .orderQuantity(detail.getOrderQuantity())
                            .totalPrice(detail.getTotalPrice())
                            .build();
                })
                .collect(Collectors.toList());

        order.addOrderDetails(orderDetails);
        order.calculateTotalAmount();

        // Order 및 OrderDetail 함께 저장
        orderRepository.save(order);

        messagingProducerService.sendPaymentRequest(
                order.getOrderId(),
                order.getAmount(),
                username
        );

        temporaryStorageService.storeDeliveryInfo(order.getOrderId(), requestDto.getDeliveryInfo());

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
}