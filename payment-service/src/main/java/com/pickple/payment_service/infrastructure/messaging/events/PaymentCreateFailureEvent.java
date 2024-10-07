package com.pickple.payment_service.infrastructure.messaging.events;

import com.pickple.payment_service.exception.PaymentErrorCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentCreateFailureEvent {
    private UUID orderId;
    private String message = "Failed to create order due to an error";
    private String errorCode = PaymentErrorCode.PAYMENT_CREATE_FAILED.getStatus().toString();
    private LocalDateTime timestamp = LocalDateTime.now();

    public PaymentCreateFailureEvent(UUID orderId) {
        this.orderId = orderId;
    }
}
