package com.pickple.commerceservice.infrastructure.feign;

import com.pickple.commerceservice.exception.CommerceErrorCode;
import com.pickple.commerceservice.infrastructure.feign.dto.DeliveryClientDto;
import com.pickple.common_module.exception.CustomException;
import com.pickple.common_module.presentation.dto.ApiResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.UUID;

@Primary
@FeignClient(name = "delivery-service")
public interface DeliveryClient {

    @CircuitBreaker(name = "deliveryService", fallbackMethod = "getDeliveryInfoFallback")
    @GetMapping("/api/v1/deliveries/orders/{orderId}")
    ApiResponse<DeliveryClientDto> getDeliveryInfo(
            @RequestHeader("X-User-Roles") String authority,
            @RequestHeader("X-User-Name") String username,
            @PathVariable("orderId") UUID orderId);

    // 서킷 브레이커 동작 시 호출될 fallback 메서드
    default ApiResponse<DeliveryClientDto> getDeliveryInfoFallback(String authority, String username, UUID orderId, Throwable throwable) {
        throw new RuntimeException("배송 서비스와의 통신이 원활하지 않습니다.", throwable);
    }
}