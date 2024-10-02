package com.pickple.delivery.application.service;

import com.pickple.common_module.exception.CommonErrorCode;
import com.pickple.common_module.exception.CustomException;
import com.pickple.common_module.infrastructure.messaging.EventSerializer;
import com.pickple.delivery.application.dto.DeliveryCreateRequestDto;
import com.pickple.delivery.application.events.DeliveryCreateFailureEvent;
import com.pickple.delivery.application.events.DeliveryCreateResponseEvent;
import com.pickple.delivery.application.dto.DeliveryStartRequestDto;
import com.pickple.delivery.application.dto.DeliveryStartResponseDto;
import com.pickple.delivery.application.mapper.DeliveryMapper;
import com.pickple.delivery.domain.model.enums.DeliveryCarrier;
import com.pickple.delivery.domain.model.enums.DeliveryType;
import com.pickple.delivery.domain.repository.DeliveryRepository;
import com.pickple.delivery.domain.model.Delivery;
import com.pickple.delivery.domain.service.DeliveryDomainService;
import com.pickple.delivery.exception.DeliveryErrorCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Slf4j
@Service
@Validated
@RequiredArgsConstructor
public class DeliveryApplicationService {

    private final DeliveryRepository deliveryRepository;

    private final DeliveryDomainService deliveryDomainService;

    private final DeliveryMessageProducerService deliveryMessageProducerService;

    @Value("${kafka.topic.delivery-create-response}")
    private String deliveryCreateResponseTopic;

    @Value("${kafka.topic.delivery-create-failure}")
    private String deliveryCreateFailureTopic;

    @Transactional
    public void createDelivery(@Valid DeliveryCreateRequestDto dto) {
        Delivery delivery;
        try {
            log.info("새로운 Delivery 를 생성합니다. 요청 정보: {}", dto);
            delivery = deliveryRepository.save(
                    deliveryDomainService.createDelivery(dto));
        } catch (Exception e) {
            log.info("Kafka 실패 메시지를 발행합니다. Topic: {}, 주문 ID: {}", deliveryCreateFailureTopic,
                    dto.getOrderId());
            DeliveryCreateFailureEvent failureEvent = new DeliveryCreateFailureEvent(
                    dto.getOrderId(), DeliveryErrorCode.DELIVERY_CREATE_FAILURE.getMessage());
            deliveryMessageProducerService.sendMessage(deliveryCreateFailureTopic,
                    EventSerializer.serialize(failureEvent));
            throw new CustomException(CommonErrorCode.DATABASE_ERROR);
        }
        DeliveryCreateResponseEvent deliveryCreateResponseEvent = new DeliveryCreateResponseEvent(
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

}
