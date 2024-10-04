package com.pickple.delivery.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.pickple.common_module.exception.CommonErrorCode;
import com.pickple.common_module.exception.CustomException;
import com.pickple.common_module.exception.ErrorResponse;
import com.pickple.delivery.domain.model.enums.DeliveryType;
import java.util.Arrays;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class DeliveryExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> customExceptionHandler(CustomException e) {
        return ResponseEntity
                .status(e.getErrorCode().getStatus())
                .body(ErrorResponse.error(e.getErrorCode()));
    }

    // @Valid 검증에 대한 에러 출력
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> methodNotValidExceptionHandler(MethodArgumentNotValidException e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST)
                .message(e.getMessage())
                .build();
        e.getBindingResult().getFieldErrors().forEach(error -> {
            errorResponse.addValidation(error.getField(), error.getDefaultMessage());
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> httpMessageNotReadableExceptionHandler(HttpMessageNotReadableException ex, WebRequest request) {
        Throwable rootCause = ex.getCause();
        if (rootCause instanceof InvalidFormatException invalidFormatException) {
            String fieldName = invalidFormatException.getPath().get(0).getFieldName();
            String invalidValue = invalidFormatException.getValue().toString();
            String message = String.format("유효하지 않는 '%s' 값입니다. '%s' 는 %s 중 하나이어야 합니다.",
                    invalidValue, fieldName, Arrays.toString(DeliveryType.values()));
            return ResponseEntity.badRequest().body(message);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(CommonErrorCode.INVALID_INPUT_VALUE);
    }
}
