package com.pickple.delivery.exception;

import com.pickple.common_module.exception.ErrorCode;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public enum DeliveryErrorCode implements ErrorCode {

    DELIVERY_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 배송입니다."),
    CARRIER_NAME_NOT_SUPPORT(HttpStatus.BAD_REQUEST, "지원하지 않는 택배사 입니다."),
    DELIVERY_TYPE_NOT_SUPPORT(HttpStatus.BAD_REQUEST, "지원하지 않는 배송 방식 입니다."),
    DELIVERY_STATUS_NOT_SUPPORT(HttpStatus.BAD_REQUEST, "지원하지 않는 배송 상태 입니다."),
    TRACKING_NUMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 송장번호 입니다."),
    INVALID_MESSAGE_FORMAT(HttpStatus.BAD_REQUEST, "유효하지 않은 메시지 형식입니다."),
    DELIVERY_CREATE_FAILURE(HttpStatus.INTERNAL_SERVER_ERROR, "Delivery 생성에 실패하였습니다."),
    DELIVERY_DELETE_FAILURE(HttpStatus.INTERNAL_SERVER_ERROR, "Delivery 삭제에 실패하였습니다."),
    DELIVERY_SAVE_FAILURE(HttpStatus.INTERNAL_SERVER_ERROR, "데이터베이스 저장에 실패하였습니다."),
    DELIVERY_ALREADY_START(HttpStatus.BAD_REQUEST, "이미 시작된 배송입니다."),
    DELIVERY_ALREADY_END(HttpStatus.BAD_REQUEST, "이미 완료된 배송입니다."),
    DELIVERY_NOT_STARTED(HttpStatus.BAD_REQUEST, "배송이 아직 시작되지 않았습니다."),
    DELIVERY_ALREADY_DELIVERED(HttpStatus.BAD_REQUEST, "이미 완료된 배송입니다.");


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
