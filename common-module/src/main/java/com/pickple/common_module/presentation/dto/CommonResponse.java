package com.pickple.common_module.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;


/**
 * CommonResponse는 API 응답을 위한 공통적인 포맷을 제공하는 클래스입니다.
 *
 * @param <T> 응답 데이터 타입
 *
 * <p>사용 예시:</p>
 * <pre>{@code
 *  // 주문 성공 응답 예시
 *  CommonResponse<OrderResponse> response = CommonResponse.success(
 *      HttpStatus.OK,
 *      "주문이 성공하였습니다.",
 *      OrderResponse.from(orderService.createOrder(request.toDto(userId)))
 *  );
 * }</pre>
 */
@Getter
@AllArgsConstructor
public class CommonResponse<T> {

    private HttpStatus status;
    private String message;
    private T data;

    public static <T> CommonResponse<T> success(HttpStatus status, String message, T data) {
        return new CommonResponse<>(status, message, data);
    }
}
