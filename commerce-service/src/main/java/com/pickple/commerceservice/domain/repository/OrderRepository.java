package com.pickple.commerceservice.domain.repository;

import com.pickple.commerceservice.application.dto.OrderByVendorResponseDto;
import com.pickple.commerceservice.domain.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
    @Query("SELECT new com.pickple.commerceservice.application.dto.OrderByVendorResponseDto(" +
            "o.orderId, o.username, o.amount, " +
            "CAST(o.orderStatus AS string), " +  // Enum을 문자열로 변환
            "od.product.productId, od.orderQuantity) " +
            "FROM Order o " +
            "JOIN o.orderDetails od " +
            "JOIN od.product p " +
            "WHERE o.isDelete = false AND p.vendor.vendorId = :vendorId")
    Page<OrderByVendorResponseDto> findOrdersByVendorId(UUID vendorId, Pageable pageable);

}