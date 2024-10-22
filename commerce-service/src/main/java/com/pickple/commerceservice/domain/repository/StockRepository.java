package com.pickple.commerceservice.domain.repository;

import com.pickple.commerceservice.domain.model.Stock;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface StockRepository extends JpaRepository<Stock, UUID> {
    Optional<Stock> findByProduct_ProductId(UUID productId);

    @Lock(LockModeType.OPTIMISTIC_FORCE_INCREMENT)
    @Query("SELECT s FROM Stock s WHERE s.product.productId = :productId")
    Optional<Stock> findByProduct_ProductIdWithLock(UUID productId);

}
