package com.pickple.commerceservice.infrastructure.feign.fallback;

import com.pickple.commerceservice.infrastructure.feign.PaymentClient;
import com.pickple.commerceservice.infrastructure.feign.dto.PaymentClientDto;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class PaymentClientFallback implements PaymentClient {

    @Override
    public PaymentClientDto getPaymentInfo(String authority, String username, UUID orderId) {
        return new PaymentClientDto(); // 기본값을 반환하거나 null 반환
    }
}
