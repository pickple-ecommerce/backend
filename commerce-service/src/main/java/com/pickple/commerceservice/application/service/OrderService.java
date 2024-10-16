package com.pickple.commerceservice.application.service;

import com.pickple.commerceservice.application.dto.OrderByVendorResponseDto;
import com.pickple.commerceservice.application.dto.OrderCreateResponseDto;
import com.pickple.commerceservice.application.dto.OrderResponseDto;
import com.pickple.commerceservice.application.dto.OrderSummaryResponseDto;
import com.pickple.commerceservice.domain.model.Order;
import com.pickple.commerceservice.domain.model.OrderDetail;
import com.pickple.commerceservice.domain.model.OrderStatus;
import com.pickple.commerceservice.domain.model.Product;
import com.pickple.commerceservice.domain.repository.OrderRepository;
import com.pickple.commerceservice.domain.repository.ProductRepository;
import com.pickple.commerceservice.exception.CommerceErrorCode;
import com.pickple.commerceservice.infrastructure.feign.DeliveryClient;
import com.pickple.commerceservice.infrastructure.feign.PaymentClient;
import com.pickple.commerceservice.infrastructure.feign.dto.DeliveryClientDto;
import com.pickple.commerceservice.infrastructure.feign.dto.PaymentClientDto;
import com.pickple.commerceservice.infrastructure.messaging.OrderMessagingProducerService;
import com.pickple.commerceservice.presentation.dto.request.OrderCreateRequestDto;
import com.pickple.common_module.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final TemporaryStorageService temporaryStorageService;
    private final OrderMessagingProducerService messagingProducerService;
    private final ProductRepository productRepository;
    private final PaymentClient paymentClient;
    private final DeliveryClient deliveryClient;

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
                    OrderDetail orderDetail = OrderDetail.builder()
                            .order(order)
                            .product(product)
                            .orderQuantity(detail.getOrderQuantity())
                            .unitPrice(product.getProductPrice())
                            .build();
                    orderDetail.calculateTotalPrice(); // 단가*수량 계산
                    return orderDetail;
                })
                .collect(Collectors.toList());

        order.addOrderDetails(orderDetails);
        order.calculateTotalAmount();

        orderRepository.save(order);

        // 결제 요청 (Kafka)
        messagingProducerService.sendPaymentRequest(order.getOrderId(), order.getAmount(), username);

        // 배송 정보 저장 (Redis)
        temporaryStorageService.storeDeliveryInfo(order.getOrderId(), requestDto.getDeliveryInfo());

        // OrderCreateResponseDto 반환 (fromEntity 메서드 활용)
        return OrderCreateResponseDto.fromEntity(order);
    }

    /**
     * 주문 단건 조회
     */
    @Transactional(readOnly = true)
    public OrderResponseDto getOrderById(UUID orderId, String role, String username) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(CommerceErrorCode.ORDER_NOT_FOUND));

        // 결제 정보 가져오기 (Feign)
        PaymentClientDto paymentInfo = paymentClient.getPaymentInfo(role, username, orderId);

        // 배송 정보 가져오기 (Feign) - 예외 처리 로직은 DeliveryClient 내부에서 처리됨
        DeliveryClientDto deliveryInfo = deliveryClient.fetchDeliveryInfo(role, username, orderId);

        // OrderResponseDto 반환 (fromEntity 메서드 활용)
        return OrderResponseDto.fromEntity(order, paymentInfo, deliveryInfo);
    }

    /**
     * 주문 취소 메소드
     */
    @Transactional
    public OrderResponseDto cancelOrder(UUID orderId, String username, String role) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(CommerceErrorCode.ORDER_NOT_FOUND));

        order.changeStatus(OrderStatus.CANCELED);
        orderRepository.save(order);

        // 결제 취소 요청 전송 (Kafka)
        messagingProducerService.sendPaymentCancelRequest(orderId);

        // 배송 정보 삭제 요청 처리
        DeliveryClientDto deliveryInfo = deliveryClient.fetchDeliveryInfo(role, username, orderId);
        if (deliveryInfo != null && deliveryInfo.getDeliveryId() != null) {
            messagingProducerService.sendDeliveryDeleteRequest(deliveryInfo.getDeliveryId(), orderId);
        }

        // OrderResponseDto 반환 (fromEntity 메서드 활용)
        return OrderResponseDto.fromEntity(order, null, null);
    }

    /**
     * Vendor ID로 주문 조회
     */
    @Transactional(readOnly = true)
    public Page<OrderByVendorResponseDto> findByVendorId(UUID vendorId, Pageable pageable) {
        return orderRepository.findOrdersByVendorId(vendorId, pageable)
                .map(OrderByVendorResponseDto::fromEntity);
    }

    /**
     * 전체 주문 조회
     */
    @Transactional(readOnly = true)
    public Page<OrderSummaryResponseDto> getAllOrders(Pageable pageable) {
        return orderRepository.findAll(pageable)
                .map(OrderSummaryResponseDto::fromEntity);
    }

    /**
     * 내 주문 조회
     */
    @Transactional(readOnly = true)
    public Page<OrderSummaryResponseDto> getOrdersByUsername(String username, Pageable pageable) {
        return orderRepository.findByUsernameAndIsDeleteFalse(username, pageable)
                .map(OrderSummaryResponseDto::fromEntity);  // DTO 변환
    }

}