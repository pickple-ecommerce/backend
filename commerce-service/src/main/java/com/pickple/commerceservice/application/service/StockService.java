package com.pickple.commerceservice.application.service;

import com.pickple.commerceservice.application.dto.StockResponseDto;
import com.pickple.commerceservice.domain.model.Product;
import com.pickple.commerceservice.domain.model.Stock;
import com.pickple.commerceservice.domain.repository.ProductRepository;
import com.pickple.commerceservice.domain.repository.StockRepository;
import com.pickple.commerceservice.exception.CommerceErrorCode;
import com.pickple.commerceservice.presentation.dto.request.StockCreateRequestDto;
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
        if (createDto == null) {
            throw new CustomException(CommerceErrorCode.STOCK_DATA_NOT_FOUND);
        }

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

}
