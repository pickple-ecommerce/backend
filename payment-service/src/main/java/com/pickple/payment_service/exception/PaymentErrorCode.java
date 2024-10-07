package com.pickple.payment_service.exception;

import com.pickple.common_module.exception.ErrorCode;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public enum PaymentErrorCode implements ErrorCode {

    PAYMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 ID의 결제 내역을 찾을 수 없습니다."),
    INVALID_MESSAGE_FORMAT(HttpStatus.BAD_REQUEST, "유효하지 않은 메시지 형식입니다."),
    PAYMENT_CREATE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "결제 생성에 실패했습니다." ),
    PAYMENT_CANCEL_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "결제 취소에 실패했습니다." );

    private final HttpStatus status;
    private final String message;

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public HttpStatus getStatus() {
        return status;
    }
}
