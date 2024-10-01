package com.pickple.payment_service.infrastructure.messaging.events;

import com.pickple.payment_service.domain.model.PaymentStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentSuccessEvent {
    private UUID paymentId;
    private PaymentStatusEnum status;

}
