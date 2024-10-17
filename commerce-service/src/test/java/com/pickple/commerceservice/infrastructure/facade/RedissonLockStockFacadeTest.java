package com.pickple.commerceservice.infrastructure.facade;

import com.pickple.commerceservice.application.service.OrderService;
import com.pickple.commerceservice.application.service.StockService;
import com.pickple.commerceservice.domain.model.Product;
import com.pickple.commerceservice.domain.model.Stock;
import com.pickple.commerceservice.domain.repository.OrderRepository;
import com.pickple.commerceservice.domain.repository.ProductRepository;
import com.pickple.commerceservice.domain.repository.StockRepository;
import com.pickple.commerceservice.presentation.dto.request.PreOrderRequestDto;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class RedissonLockStockFacadeTest {

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private StockService stockService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private RedissonLockStockFacade redissonLockStockFacade;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    private Stock stock;

    private RLock mockLock;

    private Product product;

    @BeforeEach
    public void setUp() {
        // Create a Product instance with initial values
        orderRepository.deleteAll();

    }


    @Test
    @DisplayName("동시에 100개의 요청에서 재고 감소 테스트")
    void decreaseStockQuantityWithLock() throws InterruptedException{
        // Given
        product = Product.builder()
                .productId(UUID.randomUUID())
                .productName("Test Product")
                .productPrice(BigDecimal.valueOf(1000))
                .description("Test product description")
                .productImage("image_url")
                .isPublic(true)
                .build();

        Product first = productRepository.save(product);
        stock = Stock.builder()
                .version(0L)
                .stockId(UUID.randomUUID())
                .stockQuantity(1L)
                .product(first)
                .build();

        // Assign the stock to the product
        Stock savedStock = stockRepository.save(stock);
        first.assignStock(savedStock);
        Product saved = productRepository.save(first);

        PreOrderRequestDto requestDto = new PreOrderRequestDto(saved.getProductId());

        int threadCount = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        // When
        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    orderService.createPreOrder(requestDto, "test-user");
//                    redissonLockStockFacade.decreaseStockQuantityWithLock(saved.getProductId());
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    latch.countDown();
                }
            });
        }

        // Wait for all threads to finish
        latch.await();
        executorService.shutdown();

        // Then

//        assertEquals(0L, stockRepository.findByProduct_ProductId(saved.getProductId()).get().getStockQuantity());
        assertEquals(1, orderRepository.findAll().size());
    }
}