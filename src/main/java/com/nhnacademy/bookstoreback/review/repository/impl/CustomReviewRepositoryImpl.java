package com.nhnacademy.bookstoreback.review.repository.impl;

import com.nhnacademy.bookstoreback.review.domain.entity.QReview;
import com.nhnacademy.bookstoreback.review.repository.CustomReviewRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;

public class CustomReviewRepositoryImpl implements CustomReviewRepository {

	private final JPAQueryFactory queryFactory;

	public CustomReviewRepositoryImpl(EntityManager em) {
		this.queryFactory = new JPAQueryFactory(em);
	}

	@Override
	public double getReviewsAverageScoreByBookId(Long bookId) {
		QReview review = QReview.review;
		Double avgScore = queryFactory
			.select(review.reviewScore.avg())
			.from(review)
			.where(review.book.bookId.eq(bookId))
			.fetchOne();

		if (avgScore == null) {
			return 0;
		} else {
			return Math.round(avgScore * 100.0) / 100.0;
		}
		// 소수점 두 자리까지 반올림하여 반환

	}
}
