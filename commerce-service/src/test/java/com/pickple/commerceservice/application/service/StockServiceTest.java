package com.pickple.commerceservice.application.service;

import com.pickple.commerceservice.domain.model.Stock;
import com.pickple.commerceservice.domain.repository.StockRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StockServiceTest {

    @Mock
    private StockRepository stockRepository;

    @InjectMocks
    private StockService stockService;

    private Stock stock;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        stock = Stock.builder()
                .stockId(UUID.randomUUID())
                .stockQuantity(10L)
                .build();
    }


    @Test
    void testIncreaseStockQuantity() {
        UUID productId = stock.getStockId();
        when(stockRepository.findByProduct_ProductId(productId)).thenReturn(Optional.of(stock));

        stockService.increaseStockQuantity(productId);

        verify(stockRepository).findByProduct_ProductId(productId);
        assertEquals(11L, stock.getStockQuantity()); // 수량 증가 확인
    }

    @Test
    void testDecreaseStockQuantity() {
        UUID productId = stock.getStockId();
        when(stockRepository.findByProduct_ProductId(productId)).thenReturn(Optional.of(stock));

        stockService.decreaseStockQuantity(productId);

        verify(stockRepository).findByProduct_ProductId(productId);
        assertEquals(9L, stock.getStockQuantity()); // 수량 감소 확인
    }
}