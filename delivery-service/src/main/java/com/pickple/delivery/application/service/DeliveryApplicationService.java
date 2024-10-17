package com.pickple.delivery.application.service;

import static com.pickple.delivery.infrastructure.config.RedisConfig.CACHE_PREFIX;

import com.pickple.common_module.exception.CommonErrorCode;
import com.pickple.common_module.exception.CustomException;
import com.pickple.common_module.infrastructure.messaging.EventSerializer;
import com.pickple.delivery.application.dto.request.DeliveryCreateRequestDto;
import com.pickple.delivery.application.dto.request.DeliveryUpdateRequestDto;
import com.pickple.delivery.application.dto.response.DeliveryDeleteResponseDto;
import com.pickple.delivery.application.dto.response.DeliveryInfoResponseDto;
import com.pickple.delivery.application.dto.response.DeliveryStatusResponseDto;
import com.pickple.delivery.application.events.DeliveryCreateResponseEvent;
import com.pickple.delivery.application.dto.request.DeliveryStartRequestDto;
import com.pickple.delivery.application.dto.response.DeliveryStartResponseDto;
import com.pickple.delivery.application.events.DeliveryEndEvent;
import com.pickple.delivery.application.events.NotificationSendEvent;
import com.pickple.delivery.application.mapper.DeliveryMapper;
import com.pickple.delivery.application.port.OrderClient;
import com.pickple.delivery.domain.model.deleted.DeliveryDeleted;
import com.pickple.delivery.domain.model.deleted.DeliveryDetailDeleted;
import com.pickple.delivery.domain.model.enums.DeliveryCarrier;
import com.pickple.delivery.domain.model.enums.DeliveryStatus;
import com.pickple.delivery.domain.model.enums.DeliveryType;
import com.pickple.delivery.domain.repository.DeliveryRepository;
import com.pickple.delivery.domain.repository.deleted.DeliveryDeletedRepository;
import com.pickple.delivery.domain.model.Delivery;
import com.pickple.delivery.exception.DeliveryErrorCode;
import com.pickple.delivery.infrastructure.messaging.DeliveryMessageProducerService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Slf4j
@Service
@RequiredArgsConstructor
@Validated
public class DeliveryApplicationService {

    private final DeliveryRepository deliveryRepository;

    private final DeliveryMessageProducerService deliveryMessageProducerService;

    private final DeliveryDeletedRepository deliveryDeletedRepository;

    private final RedisTemplate<String, Object> redisTemplate;

    private final OrderClient orderClient;

    @Value("${kafka.topic.delivery-create-response}")
    private String deliveryCreateResponseTopic;

    @Value("${kafka.topic.delivery-end-response}")
    private String deliveryEndRequestTopic;

    @Value("${kafka.topic.email-create-request}")
    private String emailCreateRequestTopic;

    @Transactional
    public void createDelivery(@Valid DeliveryCreateRequestDto dto) {
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

    @Transactional
    public DeliveryInfoResponseDto updateDelivery(UUID deliveryId, DeliveryUpdateRequestDto dto) {
        Delivery delivery = deliveryRepository.findById(deliveryId).orElseThrow(
                () -> new CustomException(DeliveryErrorCode.DELIVERY_NOT_FOUND)
        );
        if (delivery.getDeliveryStatus() != DeliveryStatus.PENDING) {
            throw new CustomException(DeliveryErrorCode.DELIVERY_ALREADY_START);
        }
        delivery.updateDelivery(dto);
        deliveryRepository.save(delivery);
        log.info("배송 정보가 성공적으로 업데이트되었습니다. 배송 ID: {}", delivery.getDeliveryId());
        return DeliveryMapper.convertEntityToInfoResponseDto(delivery);
    }

    @Transactional(readOnly = true)
    public DeliveryStartResponseDto startDelivery(DeliveryStartRequestDto dto) {
        log.info("배송 시작 요청을 처리합니다. 배송 ID: {}, 택배 회사: {}, 배송 유형: {}", dto.getDeliveryId(),
                dto.getDeliveryCarrier(), dto.getDeliveryType());
        Delivery delivery = deliveryRepository.findById(dto.getDeliveryId()).orElseThrow(
                () -> new CustomException(DeliveryErrorCode.DELIVERY_NOT_FOUND)
        );

        if (delivery.getDeliveryStatus() != DeliveryStatus.PENDING) {
            throw new CustomException(DeliveryErrorCode.DELIVERY_ALREADY_START);
        }

        String carrierId = dto.getDeliveryCarrier().getCompanyId();
        delivery.startDelivery(carrierId, dto.getDeliveryType(), dto);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String sender = (String) authentication.getPrincipal();
        String role =  authentication.getAuthorities().stream().findFirst().get().getAuthority();
        String username = orderClient.getUsernameByDeliveryId(
                delivery.getDeliveryId(), role, sender);

        NotificationSendEvent notificationSendEvent = NotificationSendEvent.builder()
                .username(username)
                .subject("배송 시작 알림")
                .content("배송이 시작되었습니다.")
                .sender(sender)
                .build();
        deliveryMessageProducerService.sendMessage(emailCreateRequestTopic,
                EventSerializer.serialize(notificationSendEvent));

        log.info("배송 시작 처리가 성공적으로 완료되었습니다. 배송 ID: {}", delivery.getDeliveryId());
        return DeliveryMapper.convertEntityToStartResponseDto(deliveryRepository.save(delivery));
    }

    @Transactional
    public void endDelivery(UUID deliveryId) {
        log.info("배송 완료 요청을 처리합니다. 배송 ID: {}", deliveryId);
        Delivery delivery = deliveryRepository.findById(deliveryId).orElseThrow(
                () -> new CustomException(DeliveryErrorCode.DELIVERY_NOT_FOUND)
        );

        if (delivery.getDeliveryStatus() == DeliveryStatus.PENDING) {
            throw new CustomException(DeliveryErrorCode.DELIVERY_NOT_STARTED);
        }
        if (delivery.getDeliveryStatus() == DeliveryStatus.DELIVERED) {
            throw new CustomException(DeliveryErrorCode.DELIVERY_ALREADY_DELIVERED);
        }

        delivery.endDelivery();
        deliveryRepository.save(delivery);

        deliveryMessageProducerService.sendMessage(deliveryEndRequestTopic,
                EventSerializer.serialize(
                        new DeliveryEndEvent(delivery.getOrderId(), deliveryId)
                ));
        log.info("배송 완료 처리가 성공적으로 완료되었습니다. 배송 ID: {}", delivery.getDeliveryId());
    }

    @Transactional(readOnly = true)
    public DeliveryStartResponseDto getDeliveryInfoByOrderId(UUID orderId) {
        log.info("배송 정보 요청을 처리합니다. 주문 ID: {}", orderId);
        Delivery delivery = deliveryRepository.findByOrderId(orderId).orElseThrow(
                () -> new CustomException(DeliveryErrorCode.DELIVERY_NOT_FOUND)
        );

        log.info("배송 정보 요청이 성공적으로 완료되었습니다. 주문 ID: {}", delivery.getOrderId());
        return DeliveryMapper.convertEntityToStartResponseDto(delivery);
    }

    @Transactional(readOnly = true)
    public DeliveryInfoResponseDto getDeliveryInfo(UUID deliveryId) {
        log.info("배송 정보 조회 요청을 처리합니다. 배송 ID: {}", deliveryId);

        DeliveryInfoResponseDto cachedDeliveryInfo = (DeliveryInfoResponseDto) redisTemplate.opsForValue()
                .get(
                        CACHE_PREFIX + "::" + deliveryId
                );
        if (cachedDeliveryInfo != null) {
            return cachedDeliveryInfo;
        }

        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new CustomException(DeliveryErrorCode.DELIVERY_NOT_FOUND));
        return DeliveryMapper.convertEntityToInfoResponseDto(delivery);
    }

    @Transactional(readOnly = true)
    public DeliveryStatusResponseDto getDeliveryStatus(UUID deliveryId) {
        log.info("배송 상태 조회 요청을 처리합니다. 배송 ID: {}", deliveryId);
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new CustomException(DeliveryErrorCode.DELIVERY_NOT_FOUND));
        return new DeliveryStatusResponseDto(delivery.getDeliveryStatus().getStatus());
    }

    @Transactional(readOnly = true)
    public Page<DeliveryInfoResponseDto> getAllDeliveryInfo(Pageable pageable) {
        log.info("배송 전체 정보 조회 요청을 처리합니다. 요청 정보: {}", pageable);
        return deliveryRepository.findInfoAll((pageable));
    }

    @Transactional(readOnly = true)
    public Page<DeliveryInfoResponseDto> getDeliveriesByCarrier(String carrierName,
            Pageable pageable) {
        DeliveryCarrier deliveryCarrier;
        try {
            deliveryCarrier = DeliveryCarrier.valueOf(carrierName);
        } catch (Exception e) {
            throw new CustomException(DeliveryErrorCode.CARRIER_NAME_NOT_SUPPORT);
        }
        return deliveryRepository.findByCarrierName(deliveryCarrier.getCompanyName(), pageable);
    }

    @Transactional(readOnly = true)
    public Page<DeliveryInfoResponseDto> getDeliveriesByStatus(DeliveryStatus status,
            Pageable pageable) {
        return deliveryRepository.findByDeliveryStatus(status, pageable);
    }

    @Transactional(readOnly = true)
    public Page<DeliveryInfoResponseDto> getDeliveriesByDeliveryType(DeliveryType deliveryType,
            Pageable pageable) {
        return deliveryRepository.findByDeliveryType(deliveryType, pageable);
    }

    @Transactional(readOnly = true)
    public DeliveryInfoResponseDto getDeliveriesByTrackingNumber(String trackingNumber) {
        return deliveryRepository.findByTrackingNumber(trackingNumber)
                .orElseThrow(
                        () -> new CustomException(DeliveryErrorCode.TRACKING_NUMBER_NOT_FOUND));
    }

    @Transactional
    public DeliveryDeleteResponseDto deleteDelivery(UUID deliveryId, String deleter) {
        log.error("배송을 삭제합니다. 배송 ID: {}, 배송 삭제 요청자: {}", deliveryId, deleter);
        Delivery delivery = deliveryRepository.findById(deliveryId).orElseThrow(
                () -> new CustomException(DeliveryErrorCode.DELIVERY_NOT_FOUND)
        );
        try {
            List<DeliveryDetailDeleted> deliveryDetailDeleted = delivery.getDeliveryDetails()
                    .stream().map(DeliveryDetailDeleted::fromDeliveryDetail).toList();
            DeliveryDeleted deletedDelivery = DeliveryDeleted.fromDelivery(delivery,
                    deliveryDetailDeleted);
            deletedDelivery.delete(deleter);

            deliveryDeletedRepository.save(deletedDelivery);
            deliveryRepository.deleteById(deliveryId);
        } catch (Exception e) {
            log.error("배송 삭제에 실패하였습니다.: {}", e.getMessage());
            throw new CustomException(DeliveryErrorCode.DELIVERY_SAVE_FAILURE);
        }
        return new DeliveryDeleteResponseDto(delivery.getDeliveryId(), delivery.getOrderId(),
                deleter);
    }

}
