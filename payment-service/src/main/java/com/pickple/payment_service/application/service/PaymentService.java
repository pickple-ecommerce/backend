package com.pickple.payment_service.application.service;

import com.pickple.common_module.exception.CommonErrorCode;
import com.pickple.payment_service.exception.PaymentErrorCode;
import com.pickple.common_module.exception.CustomException;
import com.pickple.payment_service.application.dto.PaymentRespDto;
import com.pickple.payment_service.domain.model.Payment;
import com.pickple.payment_service.domain.repository.PaymentRepository;
import com.pickple.payment_service.infrastructure.messaging.events.PaymentCancelFailureEvent;
import com.pickple.payment_service.infrastructure.messaging.events.PaymentCancelResponseEvent;
import com.pickple.payment_service.infrastructure.messaging.events.PaymentCreateFailureEvent;
import com.pickple.payment_service.infrastructure.messaging.events.PaymentCreateResponseEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
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

    @Autowired
    PaymentRepository paymentRepository;

    @Autowired
    private final PaymentEventService paymentEventService;

    private final AuditorAware auditorProvider;

    // 결제 생성
    @Transactional
    public void createPayment(UUID orderId, String userName, BigDecimal amount) {
        // Kafka 메세지 검증
        if(orderId == null || userName == null || amount == null) {
            throw new CustomException(PaymentErrorCode.INVALID_MESSAGE_FORMAT);
        }

        Payment payment = new Payment(orderId, userName, amount);

        try {
            // 결제 생성 후 대기 처리
            paymentRepository.save(payment);
        } catch(Exception e){
            PaymentCreateFailureEvent event = new PaymentCreateFailureEvent(orderId);
            paymentEventService.sendCreateFailureEvent(event);
            throw new CustomException(PaymentErrorCode.PAYMENT_CREATE_FAILED);
        }

        // TODO: 결제 대기 처리 후, 결제가 완료되면 Order 쪽에 결제 생성 메시지를 보내야 합니다.
        // 결제 완료 처리
        payment.success();
        paymentRepository.save(payment);

        PaymentCreateResponseEvent event = new PaymentCreateResponseEvent(payment.getOrderId(), payment.getPaymentId());
        paymentEventService.sendCreateSuccessEvent(event);

    }

    // 결제 취소 처리
    @Transactional
    public void cancelPayment(UUID orderId){
        // Kafka 메세지 검증
        if(orderId == null) {
            throw new CustomException(PaymentErrorCode.INVALID_MESSAGE_FORMAT);
        }

        Payment payment = paymentRepository.findByOrderIdAndIsDeleteIsFalse(orderId).orElseThrow(
                () -> new CustomException(PaymentErrorCode.PAYMENT_NOT_FOUND)
        );

        try {
            // 결제 취소
            payment.cancel();
            paymentRepository.save(payment);
        } catch(Exception e){
            PaymentCancelFailureEvent event = new PaymentCancelFailureEvent(orderId);
            paymentEventService.sendCancelFailureEvent(event);
            throw new CustomException(PaymentErrorCode.PAYMENT_CANCEL_FAILED);
        }

        PaymentCancelResponseEvent event = new PaymentCancelResponseEvent(payment.getOrderId(), payment.getPaymentId());
        paymentEventService.sendCancelSuccessEvent(event);
    }

    // 결제 삭제
    @Transactional
    public void deletePayment(UUID paymentId){
        Payment payment = paymentRepository.findByPaymentIdAndIsDeleteIsFalse(paymentId).orElseThrow(
                ()-> new CustomException(PaymentErrorCode.PAYMENT_NOT_FOUND)
        );

        try {
            // 논리적 삭제 처리
            payment.delete(auditorProvider.toString());
            paymentRepository.save(payment);
        }catch(Exception e){
            throw new CustomException(CommonErrorCode.DATABASE_ERROR);
        }
    }

    // 결제 단건 조회
    @Transactional(readOnly = true)
    public PaymentRespDto getPaymentDetails (UUID paymentId, String userName){
        Payment payment = paymentRepository.findByPaymentIdAndIsDeleteIsFalse(paymentId).orElseThrow(
                ()-> new CustomException(PaymentErrorCode.PAYMENT_NOT_FOUND)
        );

        // 요청한 클라이언트의 결제 내역이 맞는지 확인
        if(!payment.getUserName().equals(userName)){
            throw new CustomException(CommonErrorCode.AUTHORIZATION_ERROR);
        }

        return PaymentRespDto.from(payment);
    }

    // 결제 전체 조회 (admin)
    @Transactional(readOnly = true)
    public Page<PaymentRespDto> getAllPayments(Pageable pageable) {
        Page<Payment> paymentList = paymentRepository.findAllByIsDeleteIsFalse(pageable).orElseThrow(
                ()-> new CustomException(PaymentErrorCode.PAYMENT_NOT_FOUND)
        );

        return paymentList.map(PaymentRespDto::from);
    }

    // feign - 주문 조회 시 결제 내역 요청
    @Transactional(readOnly = true)
    public PaymentRespDto getPaymentInfo(UUID orderId){
        Payment payment = paymentRepository.findByOrderIdAndIsDeleteIsFalse(orderId).orElseThrow(
                ()-> new CustomException(PaymentErrorCode.PAYMENT_NOT_FOUND)
        );

        return PaymentRespDto.from(payment);
    }

}
