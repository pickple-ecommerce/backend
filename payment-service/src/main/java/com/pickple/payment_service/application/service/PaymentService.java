package com.pickple.payment_service.application.service;

import com.pickple.common_module.exception.CommonErrorCode;
import com.pickple.common_module.exception.CustomException;
import com.pickple.payment_service.application.dto.PaymentCreateRespDto;
import com.pickple.payment_service.domain.model.Payment;
import com.pickple.payment_service.domain.repository.PaymentRepository;
import com.pickple.payment_service.presentation.request.PaymentCreateReqDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;

    @Transactional
    public PaymentCreateRespDto createPayment(PaymentCreateReqDto reqDto) {
        Payment payment = PaymentCreateReqDto.toPayment(reqDto);

        paymentRepository.save(payment);

        return PaymentCreateRespDto.from(payment);
    }
}
