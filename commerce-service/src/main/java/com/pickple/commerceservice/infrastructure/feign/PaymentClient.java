package com.pickple.commerceservice.infrastructure.feign;

import com.pickple.commerceservice.infrastructure.feign.dto.PaymentClientDto;
import com.pickple.commerceservice.infrastructure.feign.fallback.PaymentClientFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.UUID;

@Primary
@FeignClient(name = "payment-service", fallback = PaymentClientFallback.class)
public interface PaymentClient {

    @GetMapping("/api/v1/payments/getPaymentInfo/{order_id}")
    PaymentClientDto getPaymentInfo(
            @RequestHeader("X-User-Roles") String authority,
            @RequestHeader("X-User-Name") String username,
            @PathVariable("order_id") UUID orderId);
}

