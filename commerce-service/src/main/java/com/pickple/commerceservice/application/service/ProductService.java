package com.pickple.commerceservice.application.service;

import com.pickple.commerceservice.application.dto.ProductResponseDto;
import com.pickple.commerceservice.application.dto.StockResponseDto;
import com.pickple.commerceservice.domain.model.Product;
import com.pickple.commerceservice.domain.model.Vendor;
import com.pickple.commerceservice.domain.repository.ProductRepository;
import com.pickple.commerceservice.domain.repository.StockRepository;
import com.pickple.commerceservice.domain.repository.VendorRepository;
import com.pickple.commerceservice.exception.CommerceErrorCode;
import com.pickple.commerceservice.presentation.dto.request.ProductCreateRequestDto;
import com.pickple.commerceservice.presentation.dto.request.StockCreateRequestDto;
import com.pickple.common_module.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final VendorRepository vendorRepository;
    private final StockRepository stockRepository;
    private final StockService stockService;

    // 상품 생성
    @Transactional
    public ProductResponseDto createProduct(ProductCreateRequestDto createDto) {
        Vendor vendor = vendorRepository.findByVendorIdAndIsDeleteFalse(createDto.getVendorId())
                .orElseThrow(() -> new CustomException(CommerceErrorCode.VENDOR_NOT_FOUND));

        // 상품 생성
        Product product = Product.builder()
                .productName(createDto.getProductName())
                .description(createDto.getDescription())
                .productPrice(createDto.getProductPrice())
                .productImage(createDto.getProductImage())
                .isPublic(createDto.getIsPublic())
                .vendor(vendor)
                .build();
        Product savedProduct = productRepository.save(product);

        // 재고 생성
        StockCreateRequestDto stockCreate = createDto.getStock();
        stockCreate.setProductId(savedProduct.getProductId()); // 상품 ID 설정
        StockResponseDto stockResponseDto = stockService.createStock(stockCreate);

        // 생성된 재고를 상품에 할당 후, 다시 저장
        savedProduct.assignStock(stockRepository.findById(stockResponseDto.getStockId())
                .orElseThrow(() -> new CustomException(CommerceErrorCode.STOCK_NOT_FOUND)));
        productRepository.save(savedProduct);

        // 상품 응답 DTO 생성
        return ProductResponseDto.fromEntity(savedProduct);
    }
}
