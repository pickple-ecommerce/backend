package com.pickple.commerceservice.domain.repository;

import com.pickple.commerceservice.domain.model.Vendor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface VendorRepository extends JpaRepository<Vendor, UUID>, VendorRepositoryCustom {
    Page<Vendor> findAllByIsDeleteFalse(Pageable pageable);
    Optional<Vendor> findByVendorIdAndIsDeleteFalse(UUID vendorId);
}
