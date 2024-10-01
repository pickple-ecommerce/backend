package com.pickple.payment_service.application.service;

import com.pickple.common_module.exception.CommonErrorCode;
import com.pickple.common_module.exception.CustomException;
import com.pickple.payment_service.application.dto.PaymentCreateRespDto;
import com.pickple.payment_service.domain.model.Payment;
import com.pickple.payment_service.domain.repository.PaymentRepository;
import com.pickple.payment_service.infrastructure.messaging.events.PaymentSuccessEvent;
import com.pickple.payment_service.presentation.request.PaymentCreateReqDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentEventService paymentEventService;

    @Transactional
    public void createPayment(UUID orderId, BigDecimal amount) {
        Payment payment = new Payment(orderId, amount);
        paymentRepository.save(payment);

        payment.success();
        paymentRepository.save(payment);

        PaymentSuccessEvent event = new PaymentSuccessEvent(payment.getPaymentId(), payment.getOrderId(), payment.getStatus());
        paymentEventService.sendPaymentSuccessEvent(event);

    }
}
