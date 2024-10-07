package com.pickple.payment_service.infrastructure.messaging.events;

import com.pickple.payment_service.domain.model.PaymentStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentCreateResponseEvent {
    private UUID orderId;
    private UUID paymentId;
    private String method = "CREDIT-CARD";
    private String status = "COMPLETED";

    public PaymentCreateResponseEvent(UUID orderId, UUID paymentId) {
        this.orderId = orderId;
        this.paymentId = paymentId;
    }
}
