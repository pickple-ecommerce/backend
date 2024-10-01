package com.pickple.payment_service.domain.model;

public enum PaymentStatusEnum {
    PENDING("결제 대기 중"),
    SUCCESS("결제 성공"),
    FAILED("결제 실패"),
    CANCELED("결제 취소");

    private final String status;

    PaymentStatusEnum(String status) {
        this.status = status;
    }
}
