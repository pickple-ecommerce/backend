package com.pickple.commerceservice.infrastructure.messaging.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentCancelResponseEvent {
    private UUID orderId;
    private UUID paymentId; // 결제 ID
    private String status;  // 결제 취소 상태 (예: 'CANCELED', 'FAILED')
}
