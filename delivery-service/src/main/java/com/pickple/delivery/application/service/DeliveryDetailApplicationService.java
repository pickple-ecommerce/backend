package com.pickple.delivery.application.service;

import static com.pickple.delivery.infrastructure.config.RedisConfig.CACHE_PREFIX;

import com.pickple.common_module.exception.CustomException;
import com.pickple.delivery.application.dto.request.DeliveryDetailCreateRequestDto;
import com.pickple.delivery.application.dto.response.DeliveryInfoResponseDto;
import com.pickple.delivery.application.mapper.DeliveryMapper;
import com.pickple.delivery.domain.model.Delivery;
import com.pickple.delivery.domain.model.DeliveryDetail;
import com.pickple.delivery.domain.model.enums.DeliveryStatus;
import com.pickple.delivery.domain.repository.DeliveryRepository;
import com.pickple.delivery.exception.DeliveryErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeliveryDetailApplicationService {

    private final DeliveryRepository deliveryRepository;

    @Transactional
    @CachePut(value = CACHE_PREFIX, key = "#dto.deliveryId")
    public DeliveryInfoResponseDto createDeliveryDetail(
            DeliveryDetailCreateRequestDto dto) {
        Delivery delivery = deliveryRepository.findById(dto.getDeliveryId())
                .orElseThrow(() -> new CustomException(DeliveryErrorCode.DELIVERY_NOT_FOUND));

        if (delivery.getDeliveryStatus() == DeliveryStatus.PENDING) {
            throw new CustomException(DeliveryErrorCode.DELIVERY_NOT_STARTED);
        }
        if (delivery.getDeliveryStatus() == DeliveryStatus.DELIVERED) {
            throw new CustomException(DeliveryErrorCode.DELIVERY_ALREADY_DELIVERED);
        }

        log.info("새로운 DeliveryDetail 을 생성합니다. 요청 정보: {}", dto);
        DeliveryDetail deliveryDetail = DeliveryDetail.createFrom(dto);
        delivery.addDeliveryDetail(deliveryDetail);
        deliveryRepository.save(delivery);
        return DeliveryMapper.convertEntityToInfoResponseDto(delivery);
    }

}