package com.pickple.commerceservice.domain.repository;

import com.pickple.commerceservice.domain.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
    // isDelete가 false이고, isPublic이 true인 상품들만 조회
    Page<Product> findAllByIsDeleteFalseAndIsPublicTrue(Pageable pageable);

    // 특정 ID로 조회하되, isDelete가 false이고, isPublic이 true인 상품만 조회
    Optional<Product> findByProductIdAndIsDeleteFalseAndIsPublicTrue(UUID productId);
}
