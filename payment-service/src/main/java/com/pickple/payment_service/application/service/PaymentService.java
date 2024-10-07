package com.pickple.payment_service.application.service;

import com.pickple.payment_service.exception.PaymentErrorCode;
import com.pickple.common_module.exception.CustomException;
import com.pickple.payment_service.application.dto.PaymentRespDto;
import com.pickple.payment_service.domain.model.Payment;
import com.pickple.payment_service.domain.repository.PaymentRepository;
import com.pickple.payment_service.infrastructure.messaging.events.PaymentCreateResponseEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    // 결제 생성
    @Transactional
    public void createPayment(UUID orderId, String userName, BigDecimal amount) {
        Payment payment = new Payment(orderId, userName, amount);
        paymentRepository.save(payment);
        // 결제 대기 처리

        payment.success();
        paymentRepository.save(payment);
        // 결제 완료 처리

        PaymentCreateResponseEvent event = new PaymentCreateResponseEvent(payment.getPaymentId(), payment.getStatus());
        paymentEventService.sendPaymentSuccessEvent(event);

    }

    // 결제 취소 처리
    @Transactional
    public void cancelPayment(UUID orderId, String status){
        // errorEvent 만들고 난 뒤에 검증 처리 추가 예정
        Payment payment = paymentRepository.findByOrderIdAndIsDeleteIsFalse(orderId).orElseThrow(
                () -> new CustomException(PaymentErrorCode.PAYMENT_NOT_FOUND)
        );

        payment.canceled();
        paymentRepository.save(payment);
    }

    // 결제 상세 조회
    @Transactional(readOnly = true)
    public PaymentRespDto getPaymentDetails (UUID paymentId){
        Payment payment = paymentRepository.findByPaymentIdAndIsDeleteIsFalse(paymentId).orElseThrow(
                ()-> new CustomException(PaymentErrorCode.PAYMENT_NOT_FOUND)
        );

        return PaymentRespDto.from(payment);
    }

    // 결제 전체 조회 (user)
    @Transactional(readOnly = true)
    public Page<PaymentRespDto> getPaymentByUser(String userName, Pageable pageable) {
        Page<Payment> paymentList = paymentRepository.findAllByUserNameAndIsDeleteIsFalse(userName, pageable).orElseThrow(
                ()-> new CustomException(PaymentErrorCode.PAYMENT_NOT_FOUND)
        );

        return paymentList.map(PaymentRespDto::from);
    }

    // 결제 전체 조회 (admin)
    @Transactional(readOnly = true)
    public Page<PaymentRespDto> getPaymentByAdmin(Pageable pageable) {
        Page<Payment> paymentList = paymentRepository.findAllByIsDeleteIsFalse(pageable).orElseThrow(
                ()-> new CustomException(PaymentErrorCode.PAYMENT_NOT_FOUND)
        );

        return paymentList.map(PaymentRespDto::from);
    }



}
