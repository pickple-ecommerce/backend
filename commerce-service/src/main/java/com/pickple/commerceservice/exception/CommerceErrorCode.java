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
    STOCK_DATA_NOT_FOUND_FOR_PRODUCT(HttpStatus.NOT_FOUND, "해당 상품에 대한 재고가 존재하지 않습니다."),
    STOCK_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 재고입니다."),
    STOCK_UNDER_ZERO(HttpStatus.BAD_REQUEST, "재고 수량은 반드시 0 이상이어야 합니다."),
    INSUFFICIENT_STOCK(HttpStatus.BAD_REQUEST, "재고가 부족합니다."),
    STOCK_LOCK_FAILED(HttpStatus.BAD_REQUEST, "재고 lock 획득에 실패했습니다."),
    PREORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "예약 구매 정보를 찾을 수 없습니다."),
    PRE_ORDER_NOT_FOUND_FOR_PRODUCT(HttpStatus.NOT_FOUND, "해당 상품에 대한 예약 구매 정보를 찾을 수 없습니다."),

    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 주문입니다."),
    INVALID_DELIVERY_MESSAGE_FORMAT(HttpStatus.BAD_REQUEST, "유효하지 않는 배송 메시지 형식입니다."),
    INVALID_PAYMENT_MESSAGE_FORMAT(HttpStatus.BAD_REQUEST, "유효하지 않는 결제 메시지 응답입니다."),
    PAYMENT_CREATE_FAILED(HttpStatus.BAD_GATEWAY, "결제 생성에 실패하였습니다."),
    CANNOT_CANCEL_DELIVERY_IN_PROGRESS(HttpStatus.BAD_REQUEST, "배송이 이미 진행 중이므로 취소할 수 없습니다.")
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
