package com.pickple.delivery.application.service;

import com.pickple.common_module.exception.CustomException;
import com.pickple.delivery.application.dto.request.DeliveryDetailCreateRequestDto;
import com.pickple.delivery.application.dto.response.DeliveryDetailCreateResponseDto;
import com.pickple.delivery.application.dto.DeliveryDetailInfoDto;
import com.pickple.delivery.application.mapper.DeliveryDetailMapper;
import com.pickple.delivery.domain.model.DeliveryDetail;
import com.pickple.delivery.domain.model.deleted.DeliveryDetailDeleted;
import com.pickple.delivery.domain.repository.deleted.DeliveryDetailDeletedRepository;
import com.pickple.delivery.domain.repository.DeliveryDetailRepository;
import com.pickple.delivery.domain.repository.DeliveryRepository;
import com.pickple.delivery.exception.DeliveryErrorCode;
import java.util.ArrayList;
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

    private final DeliveryDetailDeletedRepository deliveryDetailDeletedRepository;

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
        return deliveryDetailRepository.findByDeliveryDetailIdDeliveryId(deliveryId).stream()
                .map(DeliveryDetailMapper::convertEntityToInfoDto).toList();
    }

    public void deleteDeliveryDetail(UUID deliveryId, String deleter) {
        try {
            List<DeliveryDetail> deliveryDetailList = deliveryDetailRepository.findByDeliveryDetailIdDeliveryId(
                    deliveryId);
            List<DeliveryDetailDeleted> deliveryDetailDeletedList = new ArrayList<>();
            deliveryDetailList.forEach(detail -> {
                DeliveryDetailDeleted deletedDeliveryDetail = DeliveryDetailDeleted.fromDeliveryDetail(
                        detail);
                deletedDeliveryDetail.delete(deleter);
                deliveryDetailDeletedList.add(deletedDeliveryDetail);
            });
            deliveryDetailRepository.deleteAll(deliveryDetailList);
            deliveryDetailDeletedRepository.saveAll(deliveryDetailDeletedList);
        } catch (Exception e) {
            log.error("DeliveryDetail 삭제에 실패하였습니다. 요청 정보: {}", deliveryId);
            throw new CustomException(DeliveryErrorCode.DELIVERY_NOT_FOUND);
        }
    }

}