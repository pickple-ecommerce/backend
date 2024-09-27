package com.pickple.common_module.exception;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * CommonErrorCode는 애플리케이션 전반에서 발생할 수 있는 공통적인 에러 코드를 정의한 Enum입니다.
 * 각 에러는 {@link HttpStatus}와 사용자에게 전달될 메시지를 포함하고 있습니다.
 * {@code com.pickple.common.exception.CustomException} 클래스와 함께 사용될 수 있습니다.
 *
 * <p>사용 예시:</p>
 * <pre>{@code
 *  throw new CustomException(CommonErrorCode.INTERNAL_SERVER_ERROR);
 * }</pre>
 */
@AllArgsConstructor
public enum CommonErrorCode implements ErrorCode {

    // 공통 서버 에러
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부에서 문제가 발생했습니다."),

    // 데이터베이스 관련 에러
    DATABASE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "데이터베이스 처리 중 문제가 발생했습니다."),
    DATA_INTEGRITY_VIOLATION(HttpStatus.CONFLICT, "데이터 무결성 위반이 발생했습니다."),

    // 외부 서비스 호출 관련 에러
    EXTERNAL_SERVICE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "외부 서비스 호출 중 문제가 발생했습니다."),
    TIMEOUT_ERROR(HttpStatus.GATEWAY_TIMEOUT, "서버 응답 시간이 초과되었습니다."),

    // 인증 및 권한 관련 에러
    AUTHENTICATION_ERROR(HttpStatus.UNAUTHORIZED, "인증에 실패했습니다."),
    AUTHORIZATION_ERROR(HttpStatus.FORBIDDEN, "해당 리소스에 접근 권한이 없습니다."),

    // 잘못된 요청 에러
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "입력 값이 유효하지 않습니다."),

    // 지원하지 않는 기능 관련 에러
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "허용되지 않은 요청 방식입니다."),

    // 리소스 관련 에러
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "요청한 리소스를 찾을 수 없습니다."),

    // 서비스 불가 에러
    SERVICE_UNAVAILABLE(HttpStatus.SERVICE_UNAVAILABLE, "현재 서비스 이용이 불가합니다.");

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
