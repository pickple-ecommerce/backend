package com.pickple.commerceservice.domain.repository;

import com.pickple.commerceservice.domain.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductRepositoryCustom {
    Page<Product> searchByKeyword(String keyword, Pageable pageable);

}
