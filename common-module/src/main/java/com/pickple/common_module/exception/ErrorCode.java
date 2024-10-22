package com.pickple.common_module.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception 발생 시 메세지와 HttpStatus를 명시하기 위한 인터페이스 입니다.
 * {@code package com.pickple.common_module.exception.CustomException;`} 의 인자로 사용됩니다.
 * enum 클래스에서 구현하여 다음과 같이 사용될 수 있습니다.
 * <p>사용 예시:</p>
 * <pre>{@code
 * @AllArgsConstructor
 * @Getter
 * public enum CommonErrorCode implements ErrorCode {
 *
 *     // 공통 서버 에러
 *     INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부에서 문제가 발생했습니다."),
 *
 *     private final HttpStatus status;
 *     private final String message;
 *
 *     @Override
 *     public String getMessage() {
 *         return message;
 *     }
 *
 *     @Override
 *     public HttpStatus getStatus() {
 *         return status;
 *     }
 * }
 * </pre>

 */
public interface ErrorCode {
    String getMessage();
    HttpStatus getStatus();
}