package com.pickple.commerceservice.presentation.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreateRequestDto {

    private List<OrderDetail> orderDetails; // 주문 세부 항목 리스트
    private PaymentInfo paymentInfo; // 결제 정보
    private DeliveryInfo deliveryInfo; // 배송 정보

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderDetail {
        private UUID productId;
        private Long orderQuantity; // 주문 수량
        private BigDecimal totalPrice; // 해당 상품 단가
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PaymentInfo {
        private BigDecimal amount; // 결제 금액
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DeliveryInfo {
        private String deliveryType; // 배송 타입 (ENUM으로 처리)
        private String deliveryRequirement; // 배송 요청 사항
        private String recipientName; // 수령인 이름
        private String address; // 배송 주소
        private String contact; // 수령인 연락처
    }
}
