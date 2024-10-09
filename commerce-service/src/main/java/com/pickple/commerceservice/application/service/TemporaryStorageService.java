package com.pickple.commerceservice.application.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class TemporaryStorageService {

    private final Map<UUID, Object> storage = new HashMap<>();

    public void storeDeliveryInfo(UUID orderId, Object deliveryInfo) {
        // 배송 정보 저장
        storage.put(orderId, deliveryInfo);
    }

    public Object getDeliveryInfo(UUID orderId) {
        // 배송 정보 조회
        return storage.get(orderId);
    }

    public void removeDeliveryInfo(UUID orderId) {
        // 배송 정보 삭제 (배송 생성 후 삭제하는 로직 추가)
        storage.remove(orderId);
    }
}
