package com.pickple.commerceservice.domain.repository;

import com.pickple.commerceservice.domain.model.Product;
import com.pickple.commerceservice.domain.model.QProduct;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Product> searchByKeyword(String keyword, Pageable pageable) {
        QProduct product = QProduct.product;

        List<Product> products = queryFactory
                .selectFrom(product)
                .where(product.productName.containsIgnoreCase(keyword)
                        .and(product.isDelete.eq(false)))  // 논리적 삭제 필드 고려
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .selectFrom(product)
                .where(product.productName.containsIgnoreCase(keyword)
                        .and(product.isDelete.eq(false)))
                .fetchCount();

        return new PageImpl<>(products, pageable, total);
    }
}
