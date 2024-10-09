package com.pickple.commerceservice.domain.repository;

import com.pickple.commerceservice.domain.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface StockRepository extends JpaRepository<Stock, UUID> {
    @Query("SELECT s FROM Stock s JOIN s.product p WHERE p.productId = :productId")
    Optional<Stock> findByProduct_ProductId(UUID productId);

}
