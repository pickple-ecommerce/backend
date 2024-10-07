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
    private UUID orderId;
    private UUID paymentId;
    private String userName;
    private BigDecimal amount;
    private String method;
    private PaymentStatusEnum status;

    public static PaymentRespDto from(Payment payment) {
        return PaymentRespDto.builder()
                .orderId(payment.getOrderId())
                .paymentId(payment.getPaymentId())
                .userName(payment.getUserName())
                .amount(payment.getAmount())
                .method(payment.getMethod())
                .status(payment.getStatus())
                .build();
    }
}
