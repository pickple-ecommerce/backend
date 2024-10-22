package com.pickple.commerceservice.application.service;

import com.pickple.commerceservice.application.dto.PreOrderResponseDto;
import com.pickple.commerceservice.domain.model.PreOrderDetails;
import com.pickple.commerceservice.domain.model.Product;
import com.pickple.commerceservice.domain.repository.PreOrderRepository;
import com.pickple.commerceservice.domain.repository.ProductRepository;
import com.pickple.commerceservice.exception.CommerceErrorCode;
import com.pickple.commerceservice.presentation.dto.request.PreOrderCreateRequestDto;
import com.pickple.commerceservice.presentation.dto.request.PreOrderUpdateRequestDto;
import com.pickple.common_module.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PreOrderService {

    private final PreOrderRepository preOrderRepository;
    private final ProductRepository productRepository;

    // 전체 예약 구매 목록 조회
    @Transactional(readOnly = true)
    public Page<PreOrderResponseDto> getAllPreOrders(Pageable pageable) {
        Page<PreOrderDetails> preOrders = preOrderRepository.findByIsDeleteFalse(pageable);
        return preOrders.map(PreOrderResponseDto::fromEntity);
    }

    // 특정 예약 구매 정보 조회
    @Transactional(readOnly = true)
    public PreOrderResponseDto getPreOrderById(UUID preOrderId) {
        PreOrderDetails preOrder = findPreOrderById(preOrderId);
        return PreOrderResponseDto.fromEntity(preOrder);
    }

    // 특정 상품의 예약 구매 정보 등록
    @Transactional
    public PreOrderResponseDto createPreOrder(UUID productId, PreOrderCreateRequestDto requestDto) {
        Product product = findProductById(productId);

        PreOrderDetails preOrder = PreOrderDetails.builder()
                .product(product)
                .preOrderStartDate(requestDto.getPreOrderStartDate())
                .preOrderEndDate(requestDto.getPreOrderEndDate())
                .preOrderStockQuantity(requestDto.getPreOrderStockQuantity())
                .build();

        preOrderRepository.save(preOrder);
        return PreOrderResponseDto.fromEntity(preOrder);
    }

    // 특정 상품의 예약 구매 정보 조회
    @Transactional(readOnly = true)
    public PreOrderResponseDto getPreOrderByProductId(UUID productId) {
        Product product = findProductById(productId);
        PreOrderDetails preOrder = findPreOrderByProductId(productId);

        return PreOrderResponseDto.fromEntity(preOrder);
    }

    // 특정 상품의 예약 구매 정보 수정
    @Transactional
    public PreOrderResponseDto updatePreOrderByProductId(UUID productId, PreOrderUpdateRequestDto updateDto) {
        Product product = findProductById(productId);
        PreOrderDetails preOrder = findPreOrderByProductId(productId);

        preOrder.updatePreOrderDetails(updateDto.getPreOrderStartDate(), updateDto.getPreOrderEndDate(), updateDto.getPreOrderStockQuantity());
        return PreOrderResponseDto.fromEntity(preOrder);
    }

    // 특정 상품의 예약 구매 정보 삭제
    @Transactional
    public void deletePreOrderByProductId(UUID productId) {
        Product product = findProductById(productId);
        PreOrderDetails preOrder = findPreOrderByProductId(productId);

        preOrder.markAsDeleted();
    }

    // 특정 예약구매 ID로 PreOrderDetails 찾기 메서드
    private PreOrderDetails findPreOrderById(UUID preOrderId) {
        return preOrderRepository.findByPreOrderIdAndIsDeleteFalse(preOrderId)
                .orElseThrow(() -> new CustomException(CommerceErrorCode.PREORDER_NOT_FOUND));
    }

    // 특정 상품 ID로 Product 찾기 메서드
    private Product findProductById(UUID productId) {
        return productRepository.findByProductIdAndIsDeleteFalse(productId)
                .orElseThrow(() -> new CustomException(CommerceErrorCode.PRODUCT_NOT_FOUND));
    }

    // 특정 상품 ID로 PreOrderDetails 찾기 메서드
    private PreOrderDetails findPreOrderByProductId(UUID productId) {
        return preOrderRepository.findByProduct_ProductIdAndIsDeleteFalse(productId)
                .orElseThrow(() -> new CustomException(CommerceErrorCode.PRE_ORDER_NOT_FOUND_FOR_PRODUCT));
    }

}
