package com.pickple.common_module.exception;

import lombok.Getter;

/**
 * {@code RuntimeException} 을 커스텀 하기 위한 클래스입니다.
 *
 *   <p>사용 예시:</p>
 *   <pre>{@code
 *    throw new CustomException(CommonErrorCode.INTERNAL_SERVER_ERROR);
 *  }</pre>
 */
@Getter
public class CustomException extends RuntimeException {

    private final ErrorCode errorCode;
    private final String message;

    public CustomException(ErrorCode errorCode) {
        this.errorCode = errorCode;
        this.message = errorCode.getMessage();
    }
}