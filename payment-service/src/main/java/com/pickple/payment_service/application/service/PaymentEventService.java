package com.pickple.payment_service.application.service;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.pickple.payment_service.infrastructure.messaging.events.PaymentSuccessEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentEventService {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ObjectMapper mapper = new ObjectMapper();

    public void sendPaymentSuccessEvent(PaymentSuccessEvent event) {

    }

}
