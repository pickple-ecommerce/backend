package com.pickple.commerceservice.domain.repository;

import com.pickple.commerceservice.domain.model.Order;
import com.pickple.commerceservice.domain.model.OrderDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {

    @Query("SELECT od FROM Order o " +
            "JOIN o.orderDetails od " +
            "JOIN od.product p " +
            "WHERE o.isDelete = false AND p.vendor.vendorId = :vendorId")
    Page<OrderDetail> findOrdersByVendorId(UUID vendorId, Pageable pageable);

    Page<Order> findByUsernameAndIsDeleteFalse(String username, Pageable pageable);
}