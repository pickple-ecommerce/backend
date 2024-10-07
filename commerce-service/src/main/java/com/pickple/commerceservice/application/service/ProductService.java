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

        Product product = Product.builder()
                .productName(createDto.getProductName())
                .description(createDto.getDescription())
                .productPrice(createDto.getProductPrice())
                .productImage(createDto.getProductImage())
                .isPublic(createDto.getIsPublic())
                .vendor(vendor)
                .stock(null)
                .build();
        Product savedProduct = productRepository.save(product);

        // 재고 생성
        StockCreateRequestDto stockCreateRequestDto = createDto.getStock();

        stockCreateRequestDto.setProductId(savedProduct.getProductId()); // 상품 ID 설정
        StockResponseDto stockResponseDto = stockService.createStock(stockCreateRequestDto);

        // 상품 응답 DTO 생성
        return ProductResponseDto.fromEntity(savedProduct);
//                .toBuilder()
//                .stock(stockResponseDto) // 재고 정보 추가
//                .build();
    }
}
