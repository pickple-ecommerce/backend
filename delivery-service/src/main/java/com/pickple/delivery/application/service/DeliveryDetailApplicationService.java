package com.pickple.delivery.application.service;

import com.pickple.common_module.exception.CustomException;
import com.pickple.delivery.application.dto.request.DeliveryDetailCreateRequestDto;
import com.pickple.delivery.application.dto.response.DeliveryDetailCreateResponseDto;
import com.pickple.delivery.application.mapper.DeliveryDetailMapper;
import com.pickple.delivery.domain.model.Delivery;
import com.pickple.delivery.domain.model.DeliveryDetail;
import com.pickple.delivery.domain.repository.DeliveryRepository;
import com.pickple.delivery.exception.DeliveryErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeliveryDetailApplicationService {

    private final DeliveryRepository deliveryRepository;

    @Transactional
    public DeliveryDetailCreateResponseDto createDeliveryDetail(
            DeliveryDetailCreateRequestDto dto) {
        Delivery delivery = deliveryRepository.findById(dto.getDeliveryId())
                .orElseThrow(() -> new CustomException(DeliveryErrorCode.DELIVERY_NOT_FOUND));

        log.info("새로운 DeliveryDetail 을 생성합니다. 요청 정보: {}", dto);
        DeliveryDetail deliveryDetail = DeliveryDetail.createFrom(dto);
        delivery.addDeliveryDetail(deliveryDetail);
        deliveryRepository.save(delivery);
        return DeliveryDetailMapper.convertEntityToCreateResponseDto(deliveryDetail);
    }

}