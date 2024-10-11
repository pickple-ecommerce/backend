package com.pickple.notification_service.exception;

import com.pickple.common_module.exception.ErrorCode;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public enum NotificationErrorCode implements ErrorCode {

    EMAIL_CREATE_ERROR(HttpStatus.BAD_REQUEST, "email 형식에 맞지 않아 생성에 실패했습니다."),
    EMAIL_SENDING_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "email 발신에 실패했습니다.");


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
