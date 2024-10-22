package com.pickple.payment_service.infrastructure.messaging;

import com.pickple.common_module.infrastructure.messaging.EventSerializer;
import com.pickple.payment_service.application.service.PaymentService;
import com.pickple.payment_service.infrastructure.messaging.events.PaymentCancelRequestEvent;
import com.pickple.payment_service.infrastructure.messaging.events.PaymentCreateRequestEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentEventConsumer {

    private final PaymentService paymentService;

    @KafkaListener(topics="payment-create-request", groupId="payment-group")
    public void handleCreateRequest(String message) {
        PaymentCreateRequestEvent event = EventSerializer.deserialize(message, PaymentCreateRequestEvent.class);
        paymentService.createPayment(event.getOrderId(), event.getUsername(), event.getAmount());
    }

    @KafkaListener(topics="payment-cancel-request", groupId="payment-group")
    public void handleCancelRequest(String message) {
        PaymentCancelRequestEvent event = EventSerializer.deserialize(message, PaymentCancelRequestEvent.class);
        paymentService.cancelPayment(event.getOrderId());
    }
}
