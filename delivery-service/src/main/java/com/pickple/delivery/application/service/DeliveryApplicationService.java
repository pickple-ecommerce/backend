package com.pickple.delivery.application.service;

import com.pickple.common_module.exception.CommonErrorCode;
import com.pickple.common_module.exception.CustomException;
import com.pickple.common_module.infrastructure.messaging.EventSerializer;
import com.pickple.delivery.application.dto.request.DeliveryCreateRequestDto;
import com.pickple.delivery.application.dto.DeliveryDetailInfoDto;
import com.pickple.delivery.application.dto.DeliveryInfoDto;
import com.pickple.delivery.application.dto.response.DeliveryDeleteResponseDto;
import com.pickple.delivery.application.dto.response.DeliveryInfoResponseDto;
import com.pickple.delivery.application.events.DeliveryCreateResponseEvent;
import com.pickple.delivery.application.dto.request.DeliveryStartRequestDto;
import com.pickple.delivery.application.dto.response.DeliveryStartResponseDto;
import com.pickple.delivery.application.mapper.DeliveryMapper;
import com.pickple.delivery.domain.model.enums.DeliveryCarrier;
import com.pickple.delivery.domain.model.enums.DeliveryType;
import com.pickple.delivery.domain.repository.projection.DeliveryInfoProjection;
import com.pickple.delivery.domain.repository.DeliveryRepository;
import com.pickple.delivery.domain.model.Delivery;
import com.pickple.delivery.exception.DeliveryErrorCode;
import com.pickple.delivery.exception.DeliveryMessageFailureHandler;
import com.pickple.delivery.infrastructure.messaging.DeliveryMessageProducerService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeliveryApplicationService {

    private final DeliveryRepository deliveryRepository;

    private final DeliveryMessageProducerService deliveryMessageProducerService;

    private final DeliveryDetailApplicationService deliveryDetailApplicationService;

    private final DeliveryMessageFailureHandler deliveryMessageFailureHandler;

    @Value("${kafka.topic.delivery-create-response}")
    private String deliveryCreateResponseTopic;

    @Transactional
    public void createDelivery(DeliveryCreateRequestDto dto) {
        Delivery delivery;
        try {
            log.info("새로운 Delivery 를 생성합니다. 요청 정보: {}", dto);
            delivery = deliveryRepository.save(
                    Delivery.createFrom(dto));
        } catch (Exception e) {
            log.error("배송 생성에 실패하였습니다.: {}", dto, e);
            throw new CustomException(CommonErrorCode.DATABASE_ERROR);
        }

        DeliveryCreateResponseEvent deliveryCreateResponseEvent = new DeliveryCreateResponseEvent(
                delivery.getOrderId(),
                delivery.getDeliveryId());
        log.info("Kafka 메시지를 발행합니다. Topic: {}, 배송 ID: {}", deliveryCreateResponseTopic,
                delivery.getDeliveryId());
        deliveryMessageProducerService.sendMessage(deliveryCreateResponseTopic,
                EventSerializer.serialize(deliveryCreateResponseEvent));
    }

    @Transactional(readOnly = true)
    public DeliveryStartResponseDto startDelivery(DeliveryStartRequestDto dto) {
        log.info("배송 시작 요청을 처리합니다. 배송 ID: {}, 택배 회사: {}, 배송 유형: {}", dto.getDeliveryId(),
                dto.getCarrierName(), dto.getDeliveryType());
        Delivery delivery = deliveryRepository.findById(dto.getDeliveryId()).orElseThrow(
                () -> new CustomException(DeliveryErrorCode.DELIVERY_NOT_FOUND)
        );

        String carrierId = DeliveryCarrier.getIdFromCarrierName(dto.getCarrierName())
                .getCompanyId();
        DeliveryType deliveryType = DeliveryType.getDeliveryType(dto.getDeliveryType());
        delivery.startDelivery(carrierId, deliveryType, dto);

        log.info("배송 정보가 성공적으로 업데이트되었습니다. 배송 ID: {}", delivery.getDeliveryId());
        return DeliveryMapper.convertEntityToStartResponseDto(deliveryRepository.save(delivery));
    }

    public DeliveryInfoResponseDto getDeliveryInfo(UUID deliveryId) {
        log.info("배송 정보 조회 요청을 처리합니다. 배송 ID: {}", deliveryId);
        DeliveryInfoProjection projection = deliveryRepository.findInfoByDeliveryIdAndIsDeleteFalse(deliveryId)
                .orElseThrow(() -> new CustomException(DeliveryErrorCode.DELIVERY_NOT_FOUND));

        DeliveryInfoDto deliveryInfoDto = DeliveryMapper.convertProjectionToDto(projection);

        List<DeliveryDetailInfoDto> deliveryDetailInfoDtoList =
                deliveryDetailApplicationService.getDeliveryDetailInfoList(
                        deliveryId);

        return DeliveryMapper.createDeliveryInfoResponseDto(deliveryInfoDto,
                deliveryDetailInfoDtoList);
    }

    public DeliveryDeleteResponseDto deleteDelivery(UUID deliveryId, String deleter) {
        Delivery delivery = deliveryRepository.findById(deliveryId).orElseThrow(
                () -> new CustomException(DeliveryErrorCode.DELIVERY_NOT_FOUND)
        );
        try {
            deliveryDetailApplicationService.deleteDeliveryDetail(deliveryId, deleter);
            delivery.delete(deleter);
            deliveryRepository.save(delivery);
        } catch (Exception e) {
            throw new CustomException(DeliveryErrorCode.DELIVERY_SAVE_FAILURE);
        }
        return new DeliveryDeleteResponseDto(delivery.getDeliveryId(),
                delivery.getDeletedAt(), deleter);
    }

}
