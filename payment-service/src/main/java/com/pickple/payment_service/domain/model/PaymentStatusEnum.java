package com.pickple.payment_service.domain.model;

public enum PaymentStatusEnum {
    PENDING("결제 대기"),
    COMPLETED("결제 완료"),
    CANCELED("결제 취소");

    private final String status;

    PaymentStatusEnum(String status) {
        this.status = status;
    }
}
