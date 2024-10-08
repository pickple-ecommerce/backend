package com.pickple.payment_service.infrastructure.messaging.events;

import com.pickple.payment_service.exception.PaymentErrorCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentCancelFailureEvent {
    private UUID orderId;
    private String message = "Failed to cancel payment for this order due to an error";
    private String errorCode = PaymentErrorCode.PAYMENT_CANCEL_FAILED.getStatus().toString();
    private LocalDateTime timestamp = LocalDateTime.now();

    public PaymentCancelFailureEvent(UUID orderId) {
        this.orderId = orderId;
    }
}
