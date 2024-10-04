package com.pickple.user.exception;

import com.pickple.common_module.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum UserErrorCode implements ErrorCode {
    // ------- 4xx --------
    FORBIDDEN(HttpStatus.FORBIDDEN, "접근이 거부되었습니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 유저가 존재하지 않습니다."),;


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

