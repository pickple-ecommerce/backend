package com.pickple.payment_service.application.dto;

import com.pickple.payment_service.domain.model.Payment;
import com.pickple.payment_service.domain.model.PaymentStatusEnum;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRespDto {
    private UUID paymentId;
    private UUID orderId;
    private BigDecimal amount;
    private String method;
    private PaymentStatusEnum status;

    public static PaymentRespDto from(Payment payment) {
        return PaymentRespDto.builder()
                .paymentId(payment.getPaymentId())
                .orderId(payment.getOrderId())
                .amount(payment.getAmount())
                .method(payment.getMethod())
                .status(payment.getStatus())
                .build();
    }
}
