package com.pickple.commerceservice.infrastructure.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pickple.commerceservice.exception.CommerceErrorCode;
import com.pickple.commerceservice.infrastructure.configuration.EventSerializer;
import com.pickple.commerceservice.presentation.dto.request.OrderCreateRequestDto;
import com.pickple.common_module.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class TemporaryStorageService {

    private final RedisTemplate<String, Object> redisTemplate;

    public TemporaryStorageService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 배송 정보 저장
     */
    public void storeDeliveryInfoWithTTL(UUID orderId, OrderCreateRequestDto.DeliveryInfo deliveryInfo) {
        String redisKey = orderId.toString();
        String deliveryInfoJson = EventSerializer.serialize(deliveryInfo);
        redisTemplate.opsForValue().set(redisKey, deliveryInfoJson, 300, TimeUnit.SECONDS); // 5분 TTL 설정
    }

    /**
     * Redis에 저장된 배송 정보 조회
     */
    public OrderCreateRequestDto.DeliveryInfo getDeliveryInfo(UUID orderId) {
        String redisKey = orderId.toString();
        String deliveryInfoJson = (String) redisTemplate.opsForValue().get(redisKey);

        if (deliveryInfoJson == null) {
            throw new CustomException(CommerceErrorCode.DELIVERY_INFO_NOT_FOUND);
        }

        return EventSerializer.deserialize(deliveryInfoJson, OrderCreateRequestDto.DeliveryInfo.class);
    }

    /**
     * Redis에 저장된 배송 정보 삭제
     */
    public void removeDeliveryInfo(UUID orderId) {
        String redisKey = orderId.toString();
        redisTemplate.delete(redisKey);
    }
}