package com.pickple.payment_service.application.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pickple.common_module.infrastructure.messaging.EventSerializer;
import com.pickple.payment_service.infrastructure.messaging.events.PaymentCancelFailureEvent;
import com.pickple.payment_service.infrastructure.messaging.events.PaymentCancelResponseEvent;
import com.pickple.payment_service.infrastructure.messaging.events.PaymentCreateFailureEvent;
import com.pickple.payment_service.infrastructure.messaging.events.PaymentCreateResponseEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentEventService {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ObjectMapper mapper = new ObjectMapper();

    public void sendCreateSuccessEvent(PaymentCreateResponseEvent event) {
        kafkaTemplate.send("payment-create-response", EventSerializer.serialize(event));
    }

    public void sendCreateFailureEvent(PaymentCreateFailureEvent event){
        kafkaTemplate.send("payment-create-failure", EventSerializer.serialize(event));
    }

    public void sendCancelSuccessEvent(PaymentCancelResponseEvent event) {
        kafkaTemplate.send("payment-cancel-response", EventSerializer.serialize(event));
    }

    public void sendCancelFailureEvent(PaymentCancelFailureEvent event){
        kafkaTemplate.send("payment-cancel-failure", EventSerializer.serialize(event));
    }

}
