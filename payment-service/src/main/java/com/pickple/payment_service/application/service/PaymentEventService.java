package com.pickple.payment_service.application.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pickple.common_module.infrastructure.messaging.EventSerializer;
import com.pickple.payment_service.infrastructure.messaging.events.PaymentSuccessEvent;
import jdk.jfr.Event;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentEventService {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ObjectMapper mapper = new ObjectMapper();

    public void sendPaymentSuccessEvent(PaymentSuccessEvent event) {
        kafkaTemplate.send("payment-create-response", EventSerializer.serialize(event));
    }

}
