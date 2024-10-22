package com.pickple.user.domain.repository;

import com.pickple.user.domain.model.QUser;
import com.pickple.user.domain.model.User;
import com.pickple.user.presentation.request.UserSearchDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class UserRepositoryImpl implements UserRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<User> searchUsers(UserSearchDto searchDto, Pageable pageable) {

        QUser user = QUser.user;
        BooleanBuilder builder = new BooleanBuilder();

        // 검색 조건 추가
        if (searchDto.getUsername() != null) {
            builder.and(user.username.containsIgnoreCase(searchDto.getUsername()));
        }
        if (searchDto.getNickname() != null) {
            builder.and(user.nickname.containsIgnoreCase(searchDto.getNickname()));
        }
        if (searchDto.getEmail() != null) {
            builder.and(user.email.containsIgnoreCase(searchDto.getEmail()));
        }
        if (searchDto.getRole() != null) {
            builder.and(user.role.stringValue().equalsIgnoreCase(searchDto.getRole()));
        }

        // QueryDSL 검색 쿼리 작성
        List<User> results = queryFactory.selectFrom(user)
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        log.info("검색된 유저 수: " + results.size());

        // 결과의 총 개수 구하기
        long total = queryFactory.selectFrom(user)
                .where(builder)
                .fetchCount();

        return new PageImpl<>(results, pageable, total);
    }
}
