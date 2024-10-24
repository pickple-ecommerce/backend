package com.pickple.commerceservice.infrastructure.feign;

import com.pickple.commerceservice.exception.CommerceErrorCode;
import com.pickple.commerceservice.infrastructure.feign.dto.PaymentClientDto;
import com.pickple.common_module.exception.CustomException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.UUID;

@Primary
@FeignClient(name = "payment-service")
public interface PaymentClient {

    @CircuitBreaker(name = "paymentService", fallbackMethod = "fallbackGetPaymentInfo")
    @GetMapping("/api/v1/payments/getPaymentInfo/{order_id}")
    PaymentClientDto getPaymentInfo(
            @RequestHeader("X-User-Roles") String authority,
            @RequestHeader("X-User-Name") String username,
            @PathVariable("order_id") UUID orderId);

    // 서킷 브레이커가 열렸을 때 호출되는 fallback 메서드
    default PaymentClientDto fallbackGetPaymentInfo(String authority, String username, UUID orderId, Throwable throwable) {
        // CustomException으로 변경하여 예외 처리 일관성 유지
        throw new RuntimeException("결제 서비스와의 통신이 원활하지 않습니다.", throwable);
    }
}
