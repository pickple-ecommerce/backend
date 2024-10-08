package com.pickple.commerceservice.exception;

import com.pickple.common_module.exception.ErrorCode;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public enum CommerceErrorCode implements ErrorCode {
    VENDOR_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 업체입니다."),

    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 상품입니다."),
    PRODUCT_ID_NOT_FOUND(HttpStatus.NOT_FOUND, "상품 ID가 Null 입니다."),
    STOCK_DATA_NOT_FOUND(HttpStatus.BAD_REQUEST, "해당 재고 요청 데이터가 존재하지 않습니다."),
    STOCK_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 재고입니다."),
    ;


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
