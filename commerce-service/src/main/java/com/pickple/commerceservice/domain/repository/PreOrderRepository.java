package com.pickple.commerceservice.domain.repository;

import com.pickple.commerceservice.domain.model.PreOrderDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PreOrderRepository extends JpaRepository<PreOrderDetails, UUID> {

    // 특정 상품의 예약 구매 정보 조회
    Optional<PreOrderDetails> findByProduct_ProductIdAndIsDeleteFalse(UUID productId);
}
