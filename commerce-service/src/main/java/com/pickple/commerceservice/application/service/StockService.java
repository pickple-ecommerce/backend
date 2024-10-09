package com.pickple.commerceservice.application.service;

import com.pickple.commerceservice.application.dto.StockByProductDto;
import com.pickple.commerceservice.application.dto.StockResponseDto;
import com.pickple.commerceservice.domain.model.OrderDetail;
import com.pickple.commerceservice.domain.model.Product;
import com.pickple.commerceservice.domain.model.Stock;
import com.pickple.commerceservice.domain.repository.ProductRepository;
import com.pickple.commerceservice.domain.repository.StockRepository;
import com.pickple.commerceservice.exception.CommerceErrorCode;
import com.pickple.commerceservice.presentation.dto.request.StockCreateRequestDto;
import com.pickple.commerceservice.presentation.dto.request.StockUpdateRequestDto;
import com.pickple.common_module.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StockService {

    private final StockRepository stockRepository;
    private final ProductRepository productRepository;

    // 재고 생성
    @Transactional
    public StockResponseDto createStock(StockCreateRequestDto createDto) {
        // 상품을 찾기 전에 productId가 null인지 체크
        UUID productId = createDto.getProductId();
        if (productId == null) {
            throw new CustomException(CommerceErrorCode.PRODUCT_ID_NOT_FOUND);
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new CustomException(CommerceErrorCode.PRODUCT_NOT_FOUND));

        Stock stock = Stock.builder()
                .stockQuantity(createDto.getStockQuantity())
                .product(product)
                .build();

        Stock savedStock = stockRepository.save(stock);
        return StockResponseDto.fromEntity(savedStock);
    }

    // 상품 별 재고 조회
    @Transactional(readOnly = true)
    public StockByProductDto getStockByProductId(UUID productId) {
        Stock stock = findStockByProductId(productId);
        return StockByProductDto.fromEntity(stock);
    }

    // 상품 별 재고 수정
    @Transactional
    public StockByProductDto updateStockQuantity(UUID productId, StockUpdateRequestDto updateDto) {
        Stock stock = findStockByProductId(productId);
        stock.updateStockQuantity(updateDto.getStockQuantity());
        return StockByProductDto.fromEntity(stock);
    }

    // 재고 1 증가 메서드
    @Transactional
    public void increaseStockQuantity(UUID productId) {
        Stock stock = findStockByProductId(productId);
        stock.increaseStock();  // 수량 1 증가
    }

    // 재고 1 감소 메서드
    @Transactional
    public void decreaseStockQuantity(UUID productId) {
        Stock stock = findStockByProductId(productId);
        stock.decreaseStock();  // 수량 1 감소
    }

    // 주문한 수량만큼 재고 감소 메서드
    @Transactional
    public void decreaseStockQuantityForOrder(OrderDetail orderDetail) {
        UUID productId = orderDetail.getProduct().getProductId();
        Long quantityToReduce = orderDetail.getOrderQuantity();

        // 해당 상품 재고 조회
        Stock stock = findStockByProductId(productId);

        // 재고 수량 차감
        long currentQuantity = stock.getStockQuantity();
        if (currentQuantity < quantityToReduce) {
            throw new CustomException(CommerceErrorCode.INSUFFICIENT_STOCK);
        }
        stock.decreaseStockQuantity(currentQuantity - quantityToReduce);
    }

    // 상품 ID로 재고 조회 메서드
    private Stock findStockByProductId(UUID productId) {
        return stockRepository.findByProduct_ProductId(productId)
                .orElseThrow(() -> new CustomException(CommerceErrorCode.STOCK_DATA_NOT_FOUND_FOR_PRODUCT));
    }

}
