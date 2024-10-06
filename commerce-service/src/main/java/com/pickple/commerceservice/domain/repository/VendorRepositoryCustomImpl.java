package com.pickple.commerceservice.domain.repository;

import com.pickple.commerceservice.domain.model.QVendor;
import com.pickple.commerceservice.domain.model.Vendor;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringPath;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class VendorRepositoryCustomImpl implements VendorRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Vendor> searchVendors(String keyword, Pageable pageable) {
        QVendor vendor = QVendor.vendor;

        List<Vendor> vendors = queryFactory.selectFrom(vendor)
                .where(
                        vendor.isDelete.isFalse(),
                        containsKeyword(keyword, vendor.vendorName, vendor.vendorAddress)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory.selectFrom(vendor)
                .where(
                        vendor.isDelete.isFalse(),
                        containsKeyword(keyword, vendor.vendorName, vendor.vendorAddress)
                )
                .fetchCount();

        return new PageImpl<>(vendors, pageable, total);
    }

    private BooleanExpression containsKeyword(String keyword, StringPath... fields) {
        if (keyword == null || keyword.isEmpty()) {
            return null;
        }
        BooleanExpression expression = Expressions.FALSE;
        for (StringPath field : fields) {
            expression = expression.or(field.containsIgnoreCase(keyword));
        }
        return expression;
    }
}
