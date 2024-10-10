package com.pickple.commerceservice.application.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pickple.commerceservice.presentation.dto.request.OrderCreateRequestDto;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TemporaryStorageService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    public TemporaryStorageService(RedisTemplate<String, Object> redisTemplate, ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    public OrderCreateRequestDto.DeliveryInfo getDeliveryInfo(UUID orderId) {
        String deliveryInfoJson = (String) redisTemplate.opsForValue().get(orderId.toString());

        try {
            return objectMapper.readValue(deliveryInfoJson, OrderCreateRequestDto.DeliveryInfo.class);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("역직렬화 에러.", e);
        }
    }

    public void storeDeliveryInfo(UUID orderId, OrderCreateRequestDto.DeliveryInfo deliveryInfo) {
        try {
            String deliveryInfoJson = objectMapper.writeValueAsString(deliveryInfo);
            redisTemplate.opsForValue().set(orderId.toString(), deliveryInfoJson);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("직렬화 에러.", e);
        }
    }

    public void removeDeliveryInfo(UUID orderId) {
        redisTemplate.delete(orderId.toString());
    }
}