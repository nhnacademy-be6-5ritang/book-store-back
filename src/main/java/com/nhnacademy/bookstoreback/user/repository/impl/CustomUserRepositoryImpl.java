
package com.nhnacademy.bookstoreback.user.repository.impl;

import java.util.List;

import com.nhnacademy.bookstoreback.user.domain.dto.response.BirthdayCouponTargetResponse;
import com.nhnacademy.bookstoreback.user.domain.entity.QUser;
import com.nhnacademy.bookstoreback.user.repository.CustomUserRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;

public class CustomUserRepositoryImpl implements CustomUserRepository {

    private final JPAQueryFactory queryFactory;

    public CustomUserRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }



    @Override
    public List<BirthdayCouponTargetResponse> findUsersWithBirthMonthDay(int month, int day) {
        QUser user = QUser.user;

        return queryFactory
            .select(Projections.constructor(BirthdayCouponTargetResponse.class, user.id, user.birth))
            .from(user)
            .where(
                user.birth.month().eq(month),
                user.birth.dayOfMonth().eq(day)
            )
            .fetch();
    }
}
