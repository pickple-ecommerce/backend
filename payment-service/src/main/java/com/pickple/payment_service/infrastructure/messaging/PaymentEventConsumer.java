package com.pickple.payment_service.infrastructure.messaging;

import com.pickple.common_module.infrastructure.messaging.EventSerializer;
import com.pickple.payment_service.application.service.PaymentService;
import com.pickple.payment_service.infrastructure.messaging.events.OrderCreatedEvent;
import com.pickple.payment_service.infrastructure.messaging.events.PaymentCanceledEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentEventConsumer {

    private final PaymentService paymentService;

    @KafkaListener(topics="payment-create-request", groupId="payment-group")
    public void handleOrderCreated(String message) {
        OrderCreatedEvent event = EventSerializer.deserialize(message, OrderCreatedEvent.class);
        paymentService.createPayment(event.getOrderId(), event.getUserId(), event.getAmount());
    }

    @KafkaListener(topics="payment-cancel-request", groupId="payment-group")
    public void handlePaymentCanceled(String message) {
        PaymentCanceledEvent event = EventSerializer.deserialize(message, PaymentCanceledEvent.class);
        paymentService.cancelPayment(event.getOrderId(), event.getStatus());
    }
}
