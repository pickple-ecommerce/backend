package com.pickple.payment_service.infrastructure.messaging;

import com.pickple.payment_service.EventSerializer;
import com.pickple.payment_service.application.service.PaymentService;
import com.pickple.payment_service.infrastructure.messaging.events.OrderCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentEventConsumer {

    private final PaymentService paymentService;

    @KafkaListener(topics="order-created", groupId="payment-group")
    public void handleOrderCreated(String message) {
        OrderCreatedEvent event = EventSerializer.deserialize(message, OrderCreatedEvent.class);
        paymentService.createPayment(event.getOrderId(), event.getAmount());
    }
}
