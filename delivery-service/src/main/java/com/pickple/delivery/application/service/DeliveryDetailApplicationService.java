package com.pickple.delivery.application.service;

import com.pickple.common_module.exception.CustomException;
import com.pickple.delivery.application.dto.request.DeliveryDetailCreateRequestDto;
import com.pickple.delivery.application.dto.response.DeliveryDetailCreateResponseDto;
import com.pickple.delivery.application.dto.DeliveryDetailInfoDto;
import com.pickple.delivery.application.mapper.DeliveryDetailMapper;
import com.pickple.delivery.domain.model.DeliveryDetail;
import com.pickple.delivery.domain.repository.projection.DeliveryDetailInfoProjection;
import com.pickple.delivery.domain.repository.DeliveryDetailRepository;
import com.pickple.delivery.domain.repository.DeliveryRepository;
import com.pickple.delivery.exception.DeliveryErrorCode;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeliveryDetailApplicationService {

    private final DeliveryRepository deliveryRepository;

    private final DeliveryDetailRepository deliveryDetailRepository;

    @Transactional
    public DeliveryDetailCreateResponseDto createDeliveryDetail(
            DeliveryDetailCreateRequestDto dto) {
        if (!deliveryRepository.existsById(dto.getDeliveryId())) {
            throw new CustomException(DeliveryErrorCode.DELIVERY_NOT_FOUND);
        }

        log.info("새로운 DeliveryDetail 을 생성합니다. 요청 정보: {}", dto);
        DeliveryDetail deliveryDetail = deliveryDetailRepository.save(
                DeliveryDetail.createFrom(dto));
        return DeliveryDetailMapper.convertEntityToCreateResponseDto(deliveryDetail);
    }

    public List<DeliveryDetailInfoDto> getDeliveryDetailInfoList(UUID deliveryId) {
        Collection<DeliveryDetailInfoProjection> deliveryInfoDtoList =
                deliveryDetailRepository.findInfoByDeliveryDetailIdDeliveryId(
                        deliveryId);
        return deliveryInfoDtoList.stream().map(DeliveryDetailMapper::convertProjectionToDto)
                .toList();
    }

}