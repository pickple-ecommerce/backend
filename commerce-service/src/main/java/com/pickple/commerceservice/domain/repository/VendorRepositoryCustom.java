package com.pickple.commerceservice.domain.repository;

import com.pickple.commerceservice.domain.model.Vendor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface VendorRepositoryCustom {
    Page<Vendor> searchVendors(String keyword, Pageable pageable);
}
