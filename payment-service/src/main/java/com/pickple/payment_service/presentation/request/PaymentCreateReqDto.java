package com.pickple.payment_service.presentation.request;

import com.pickple.payment_service.domain.model.Payment;
import com.pickple.payment_service.domain.model.PaymentStatusEnum;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentCreateReqDto {
    private UUID orderId;
    private int amount;

    public static Payment toPayment(PaymentCreateReqDto reqDto) {
        return Payment.builder()
                .orderId(reqDto.getOrderId())
                .amount(reqDto.getAmount())
                .method("CARD")
                .status(PaymentStatusEnum.PENDING)
                .build();
    }
}
