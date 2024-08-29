package com.nhnacademy.bookstoreback.review.repository.impl;

import static com.nhnacademy.bookstoreback.book.domain.entity.QBook.*;
import static com.nhnacademy.bookstoreback.image.domain.entity.QImage.*;
import static com.nhnacademy.bookstoreback.order.domain.entity.QBookOrder.*;
import static com.nhnacademy.bookstoreback.order.domain.entity.QOrder.*;
import static com.nhnacademy.bookstoreback.order.domain.entity.QOrderStatus.*;
import static com.nhnacademy.bookstoreback.review.domain.entity.QReview.*;
import static com.nhnacademy.bookstoreback.review.domain.entity.QReviewImage.*;
import static com.nhnacademy.bookstoreback.user.domain.entity.QUser.*;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.nhnacademy.bookstoreback.global.util.SortUtil;
import com.nhnacademy.bookstoreback.review.domain.dto.response.GetBookOrderWithoutReviewResponse;
import com.nhnacademy.bookstoreback.review.domain.dto.response.GetReviewResponse;
import com.nhnacademy.bookstoreback.review.repository.CustomReviewRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomReviewRepositoryImpl implements CustomReviewRepository {
	private final JPAQueryFactory queryFactory;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<GetReviewResponse> getReview(Long reviewId) {
		GetReviewResponse result = queryFactory.select(Projections.constructor(
				GetReviewResponse.class,
				review.reviewId,
				review.bookOrder.orderListId,
				user.name,
				review.reviewScore,
				review.reviewComment,
				review.reviewCreatedAt,
				image.imageUrl))
			.from(review)
			.leftJoin(reviewImage).on(review.eq(reviewImage.review))
			.leftJoin(reviewImage.image, image)
			.join(review.user, user)
			.where(review.reviewId.eq(reviewId))
			.fetchOne();

		return Optional.ofNullable(result);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Page<GetReviewResponse> getReviews(Pageable pageable) {
		List<GetReviewResponse> result =
			queryFactory.select(Projections.constructor(
					GetReviewResponse.class,
					review.reviewId,
					review.bookOrder.orderListId,
					user.name,
					review.reviewScore,
					review.reviewComment,
					review.reviewCreatedAt,
					image.imageUrl))
				.from(review)
				.leftJoin(reviewImage).on(review.eq(reviewImage.review))
				.leftJoin(reviewImage.image, image)
				.offset(pageable.getOffset()) // 페이지 시작점
				.limit(pageable.getPageSize()) // 페이지 크기
				.orderBy(SortUtil.getSort(pageable, review))
				.fetch();

		Long total = Optional.ofNullable(queryFactory
			.select(review.count())
			.from(review)
			.fetchOne()).orElse(0L);

		return new PageImpl<>(result, pageable, total);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Page<GetReviewResponse> getGeneralReviews(Pageable pageable) {
		List<GetReviewResponse> result =
			queryFactory.select(Projections.constructor(
					GetReviewResponse.class,
					review.reviewId,
					review.bookOrder.orderListId,
					review.user.name,
					review.reviewScore,
					review.reviewComment,
					review.reviewCreatedAt,
					image.imageUrl)) // imageUrl 은 null
				.from(review)
				.leftJoin(reviewImage).on(review.eq(reviewImage.review)) // 리뷰와 리뷰 이미지를 LEFT JOIN
				.leftJoin(reviewImage.image, image) // 리뷰 이미지와 이미지를 LEFT JOIN
				.where(reviewImage.image.isNull()) // 이미지를 가지지 않은 리뷰만 필터링
				.offset(pageable.getOffset()) // 페이지 시작점
				.limit(pageable.getPageSize()) // 페이지 크기
				.orderBy(SortUtil.getSort(pageable, review)) // 정렬
				.fetch();

		Long total = Optional.ofNullable(queryFactory
			.select(review.count())
			.from(review)
			.leftJoin(reviewImage).on(review.eq(reviewImage.review))
			.leftJoin(reviewImage.image, image)
			.where(reviewImage.image.isNull()) // 이미지를 가지지 않은 리뷰 수 계산
			.fetchOne()).orElse(0L);

		return new PageImpl<>(result, pageable, total);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Page<GetReviewResponse> getPhotoReviews(Pageable pageable) {
		List<GetReviewResponse> result =
			queryFactory.select(Projections.constructor(
					GetReviewResponse.class,
					review.reviewId,
					review.bookOrder.orderListId,
					review.user.name,
					review.reviewScore,
					review.reviewComment,
					review.reviewCreatedAt,
					image.imageUrl)) // 이미지 URL 포함
				.from(review)
				.join(reviewImage).on(review.eq(reviewImage.review)) // 리뷰와 리뷰 이미지를 INNER JOIN
				.join(reviewImage.image, image) // 리뷰 이미지와 이미지를 INNER JOIN
				.offset(pageable.getOffset()) // 페이지 시작점
				.limit(pageable.getPageSize()) // 페이지 크기
				.orderBy(SortUtil.getSort(pageable, review)) // 정렬
				.fetch();

		Long total = Optional.ofNullable(queryFactory
			.select(review.count())
			.from(review)
			.join(reviewImage).on(review.eq(reviewImage.review))
			.join(reviewImage.image, image)
			.fetchOne()).orElse(0L); // 이미지를 가진 리뷰의 수를 계산

		return new PageImpl<>(result, pageable, total);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Page<GetReviewResponse> getReviewsByBookId(Long bookId, Pageable pageable) {
		// 리뷰와 관련된 정보를 조회하는 쿼리 작성
		List<GetReviewResponse> result = queryFactory
			.select(Projections.constructor(
				GetReviewResponse.class,
				review.reviewId,
				review.bookOrder.orderListId,
				review.user.name,
				review.reviewScore,
				review.reviewComment,
				review.reviewCreatedAt,
				image.imageUrl // 이미지 URL 을 포함
			))
			.from(review)
			.leftJoin(reviewImage).on(review.eq(reviewImage.review)) // 리뷰와 이미지 연결
			.leftJoin(reviewImage.image, image) // 이미지 테이블과 조인
			.where(review.bookOrder.book.bookId.eq(bookId)) // bookId로 필터링
			.offset(pageable.getOffset()) // 페이지 시작점
			.limit(pageable.getPageSize()) // 페이지 크기
			.orderBy(SortUtil.getSort(pageable, review)) // 정렬 설정
			.fetch(); // 결과 조회

		// 총 리뷰 수를 조회하여 페이지네이션 처리
		Long total = Optional.ofNullable(queryFactory
			.select(review.count())
			.from(review)
			.where(review.bookOrder.book.bookId.eq(bookId)) // bookId로 필터링
			.fetchOne()).orElse(0L);

		return new PageImpl<>(result, pageable, total);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Page<GetReviewResponse> getGeneralReviewsByBookId(Long bookId, Pageable pageable) {
		List<GetReviewResponse> result =
			queryFactory.select(Projections.constructor(
					GetReviewResponse.class,
					review.reviewId,
					review.bookOrder.orderListId,
					review.user.name,
					review.reviewScore,
					review.reviewComment,
					review.reviewCreatedAt,
					image.imageUrl)) // imageUrl 은 null
				.from(review)
				.leftJoin(reviewImage).on(review.eq(reviewImage.review)) // 리뷰와 리뷰 이미지를 LEFT JOIN
				.leftJoin(reviewImage.image, image) // 리뷰 이미지와 이미지를 LEFT JOIN
				.where(reviewImage.image.isNull()) // 이미지를 가지지 않은 리뷰만 필터링
				.where(review.bookOrder.book.bookId.eq(bookId)) // bookId로 필터링
				.offset(pageable.getOffset()) // 페이지 시작점
				.limit(pageable.getPageSize()) // 페이지 크기
				.orderBy(SortUtil.getSort(pageable, review)) // 정렬
				.fetch();

		Long total = Optional.ofNullable(queryFactory
			.select(review.count())
			.from(review)
			.leftJoin(reviewImage).on(review.eq(reviewImage.review))
			.leftJoin(reviewImage.image, image)
			.where(reviewImage.image.isNull()) // 이미지를 가지지 않은 리뷰 수 계산
			.where(review.bookOrder.book.bookId.eq(bookId)) // bookId로 필터링
			.fetchOne()).orElse(0L);

		return new PageImpl<>(result, pageable, total);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Page<GetReviewResponse> getPhotoReviewsByBookId(Long bookId, Pageable pageable) {
		List<GetReviewResponse> result =
			queryFactory.select(Projections.constructor(
					GetReviewResponse.class,
					review.reviewId,
					review.bookOrder.orderListId,
					review.user.name,
					review.reviewScore,
					review.reviewComment,
					review.reviewCreatedAt,
					image.imageUrl)) // 이미지 URL 포함
				.from(review)
				.join(reviewImage).on(review.eq(reviewImage.review)) // 리뷰와 리뷰 이미지를 INNER JOIN
				.join(reviewImage.image, image) // 리뷰 이미지와 이미지를 INNER JOIN
				.where(review.bookOrder.book.bookId.eq(bookId)) // bookId로 필터링
				.offset(pageable.getOffset()) // 페이지 시작점
				.limit(pageable.getPageSize()) // 페이지 크기
				.orderBy(SortUtil.getSort(pageable, review)) // 정렬
				.fetch();

		Long total = Optional.ofNullable(queryFactory
			.select(review.count())
			.from(review)
			.join(reviewImage).on(review.eq(reviewImage.review))
			.join(reviewImage.image, image)
			.where(review.bookOrder.book.bookId.eq(bookId)) // bookId로 필터링
			.fetchOne()).orElse(0L); // 이미지를 가진 리뷰의 수를 계산

		return new PageImpl<>(result, pageable, total);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Page<GetReviewResponse> getReviewsByUserId(Long userId, Pageable pageable) {
		// 리뷰를 조회하고 페이지네이션을 적용한 결과를 가져옵니다.
		List<GetReviewResponse> result = queryFactory
			.select(Projections.constructor(
				GetReviewResponse.class,
				review.reviewId,
				review.bookOrder.orderListId,
				review.user.name,
				review.reviewScore,
				review.reviewComment,
				review.reviewCreatedAt,
				image.imageUrl))
			.from(review)
			.leftJoin(reviewImage).on(review.eq(reviewImage.review))
			.leftJoin(reviewImage.image, image)
			.where(review.user.id.eq(userId))
			.offset(pageable.getOffset()) // 페이지 시작점
			.limit(pageable.getPageSize()) // 페이지 크기
			.orderBy(SortUtil.getSort(pageable, review)) // 정렬 설정
			.fetch();

		// 전체 리뷰의 총 개수를 조회합니다.
		Long total = Optional.ofNullable(queryFactory
			.select(review.count())
			.from(review)
			.where(review.user.id.eq(userId))
			.fetchOne()).orElse(0L);

		// 결과를 Page 객체로 감싸서 반환합니다.
		return new PageImpl<>(result, pageable, total);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Page<GetReviewResponse> getGeneralReviewsByUserId(Long userId, Pageable pageable) {
		// 사용자 ID에 해당하는 일반 리뷰를 조회하고 페이지네이션을 적용한 결과를 가져옵니다.
		List<GetReviewResponse> result = queryFactory
			.select(Projections.constructor(
				GetReviewResponse.class,
				review.reviewId,
				review.bookOrder.orderListId,
				review.user.name,
				review.reviewScore,
				review.reviewComment,
				review.reviewCreatedAt,
				image.imageUrl))
			.from(review)
			.leftJoin(reviewImage).on(review.eq(reviewImage.review))
			.leftJoin(reviewImage.image, image)
			.where(review.user.id.eq(userId).and(reviewImage.review.isNull())) // 이미지가 없는 리뷰만 선택
			.offset(pageable.getOffset()) // 페이지 시작점
			.limit(pageable.getPageSize()) // 페이지 크기
			.orderBy(SortUtil.getSort(pageable, review)) // 정렬 설정
			.fetch();

		// 전체 리뷰의 총 개수를 조회합니다.
		Long total = Optional.ofNullable(queryFactory
			.select(review.count())
			.from(review)
			.leftJoin(reviewImage).on(review.eq(reviewImage.review))
			.where(review.user.id.eq(userId)
				.and(reviewImage.review.isNull())) // 이미지가 없는 리뷰만 선택
			.fetchOne()).orElse(0L);

		// 결과를 Page 객체로 감싸서 반환합니다.
		return new PageImpl<>(result, pageable, total);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Page<GetReviewResponse> getPhotoReviewsByUserId(Long userId, Pageable pageable) {
		List<GetReviewResponse> result =
			queryFactory.select(Projections.constructor(
					GetReviewResponse.class,
					review.reviewId,
					review.bookOrder.orderListId,
					review.user.name,
					review.reviewScore,
					review.reviewComment,
					review.reviewCreatedAt,
					image.imageUrl)) // 이미지 URL 포함
				.from(review)
				.join(reviewImage).on(review.eq(reviewImage.review)) // 리뷰와 리뷰 이미지를 INNER JOIN
				.join(reviewImage.image, image) // 리뷰 이미지와 이미지를 INNER JOIN
				.where(review.user.id.eq(userId))
				.offset(pageable.getOffset()) // 페이지 시작점
				.limit(pageable.getPageSize()) // 페이지 크기
				.orderBy(SortUtil.getSort(pageable, review)) // 정렬
				.fetch();

		Long total = Optional.ofNullable(queryFactory
			.select(review.count())
			.from(review)
			.join(reviewImage).on(review.eq(reviewImage.review))
			.join(reviewImage.image, image)
			.where(review.user.id.eq(userId))
			.fetchOne()).orElse(0L); // 이미지를 가진 리뷰의 수를 계산

		return new PageImpl<>(result, pageable, total);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double getReviewsAverageScoreByBookId(Long bookId) {
		Double averageScore = queryFactory
			.select(review.reviewScore.avg())
			.from(review)
			.join(review.bookOrder, bookOrder)
			.where(bookOrder.book.bookId.eq(bookId))
			.fetchOne();

		return averageScore != null ? averageScore : 0.0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<GetBookOrderWithoutReviewResponse> getBooksWithoutReviewsByUserId(Long userId) {
		return queryFactory
			.select(Projections.constructor(
				GetBookOrderWithoutReviewResponse.class,
				bookOrder.orderListId,
				bookOrder.book.bookTitle
			))
			.from(bookOrder)
			.join(bookOrder.order, order)
			.join(order.user, user)
			.join(bookOrder.book, book)
			.where(user.id.eq(userId)
				.and(bookOrder.orderListId.in(
					JPAExpressions.select(bookOrder.orderListId)
						.from(bookOrder)
						.join(bookOrder.book, book)
						.join(bookOrder.order, order)
						.join(bookOrder.order.orderStatus, orderStatus)
						.where(orderStatus.orderStatusName.eq("배송 완료")
							.and(bookOrder.order.user.id.eq(userId))))
				)
				.and(book.bookId.notIn(
					JPAExpressions.select(book.bookId)
						.from(review)
						.join(review.bookOrder, bookOrder)
						.join(bookOrder.book, book)
						.join(review.user, user)
						.where(review.user.id.eq(userId))
				)))
			.fetch();
	}
}
