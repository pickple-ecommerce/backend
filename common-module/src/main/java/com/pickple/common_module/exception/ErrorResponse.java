package com.pickple.common_module.exception;

import java.util.HashMap;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * ErrorResponse는 예외가 발생했을 때 클라이언트에게 반환될 응답 객체입니다.
 * 상태 코드, 에러 메시지, 그리고 유효성 검증 오류 정보를 포함할 수 있습니다.
 * 응답 body 에 포함시킬 수 있습니다.
 *
 * <p>사용 예시:</p>
 * <pre>{@code
 * @ExceptionHandler(CustomException.class)
 * public ResponseEntity<ErrorResponse> customExceptionHandler(CustomException e) {
 *   return ResponseEntity
 *      .status(e.getErrorCode().getStatus())
 *      .body(ErrorResponse.error(e.getErrorCode()));
 * }
 *
 * // @Valid 검증에 대한 에러 출력
 * @ExceptionHandler(MethodArgumentNotValidException.class)
 * public ResponseEntity<ErrorResponse> methodNotValidExceptionHandler(MethodArgumentNotValidException e) {
 *      ErrorResponse errorResponse = ErrorResponse.builder()
 *          .status(HttpStatus.BAD_REQUEST)
 *          .message(e.getMessage())
 *          .build();
 *      e.getBindingResult().getFieldErrors().forEach(error -> {
 *          errorResponse.addValidation(error.getField(), error.getDefaultMessage());
 *      });
 *      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
 *  }
 * }</pre>
 */
@Getter
@Builder
public class ErrorResponse {

    private HttpStatus status;
    private String message;
    private Map<String, String> validation;

    public static ErrorResponse error(ErrorCode errorCode) {
        return ErrorResponse.builder()
                .status(errorCode.getStatus())
                .message(errorCode.getMessage()).build();
    }

    public void addValidation(String field, String errorMessage) {
        if (validation == null) {
            validation = new HashMap<>();
        }
        this.validation.put(field, errorMessage);
    }
}