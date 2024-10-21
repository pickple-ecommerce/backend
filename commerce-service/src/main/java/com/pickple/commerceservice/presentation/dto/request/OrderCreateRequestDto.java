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

    private List<OrderDetail> orderDetails;
    private DeliveryInfo deliveryInfo;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderDetail {
        private UUID productId;
        private Long orderQuantity; // 주문 수량
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DeliveryInfo {
        private String deliveryRequirement; // 배송 요청 사항
        private String recipientName; // 수령인 이름
        private String address; // 배송 주소
        private String contact; // 수령인 연락처
    }
}
