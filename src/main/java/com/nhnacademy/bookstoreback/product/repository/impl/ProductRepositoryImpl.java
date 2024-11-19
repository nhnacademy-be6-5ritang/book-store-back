package com.nhnacademy.bookstoreback.product.repository.impl;

import static com.nhnacademy.bookstoreback.book.domain.entity.QBook.*;
import static com.nhnacademy.bookstoreback.book.domain.entity.QBookImage.*;
import static com.nhnacademy.bookstoreback.category.domain.entity.QBookCategory.*;
import static com.nhnacademy.bookstoreback.category.domain.entity.QCategory.*;
import static com.nhnacademy.bookstoreback.image.domain.entity.QImage.*;
import static com.nhnacademy.bookstoreback.order.domain.entity.QBookOrder.*;
import static com.nhnacademy.bookstoreback.review.domain.entity.QReview.*;
import static com.nhnacademy.bookstoreback.tag.domain.entity.QBookTag.*;
import static com.nhnacademy.bookstoreback.tag.domain.entity.QTag.*;
import static com.nhnacademy.bookstoreback.wishlist.domain.entity.QWishList.*;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.nhnacademy.bookstoreback.global.util.SortUtil;
import com.nhnacademy.bookstoreback.product.dto.response.GetProductResponse;
import com.nhnacademy.bookstoreback.product.dto.response.GetProductSimpleResponse;
import com.nhnacademy.bookstoreback.product.repository.ProductRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepository {
	private final JPAQueryFactory queryFactory;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<GetProductSimpleResponse> getBestSellerBooks() {
		return queryFactory
			.select(Projections.constructor(
				GetProductSimpleResponse.class,
				book.bookId,
				book.author.authorName,
				book.bookTitle,
				book.bookPrice,
				book.bookSalePrice,
				book.bookSalePercent,
				image.imageUrl))
			.from(bookOrder)
			.join(bookOrder.book, book)  // BookOrder 와 Book 을 조인
			.leftJoin(bookImage).on(book.eq(bookImage.book))  // Book 을 기준으로 BookImage 와 조인
			.groupBy(book.bookId, book.author.authorName, book.bookTitle, book.bookPrice, book.bookSalePrice,
				book.bookSalePercent, image.imageUrl)  // Book 의 정보로 그룹화
			.orderBy(bookOrder.bookQuantity.sum().desc())  // 책의 총 주문 수량을 기준으로 내림차순 정렬
			.limit(10)  // 상위 10권으로 결과 제한
			.fetch();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<GetProductSimpleResponse> getLikesBooks() {
		return queryFactory
			.select(Projections.constructor(
				GetProductSimpleResponse.class,
				book.bookId,
				book.author.authorName,
				book.bookTitle,
				book.bookPrice,
				book.bookSalePrice,
				book.bookSalePercent,
				image.imageUrl))
			.from(wishList)
			.join(wishList.book, book)  // wishList 와 book 을 조인
			.leftJoin(bookImage).on(book.eq(bookImage.book))  // book 을 기준으로 bookImage 와 조인
			.groupBy(book.bookId, book.author.authorName, book.bookTitle, book.bookPrice, book.bookSalePrice,
				book.bookSalePercent, image.imageUrl)  // book 의 정보로 그룹화
			.orderBy(wishList.count().desc())  // 위시리스트에 가장 많이 포함된 순으로 정렬
			.limit(10)  // 상위 10권으로 결과 제한
			.fetch();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<GetProductSimpleResponse> getNewestBooks() {
		return queryFactory
			.select(Projections.constructor(
				GetProductSimpleResponse.class,
				book.bookId,
				book.author.authorName,
				book.bookTitle,
				book.bookPrice,
				book.bookSalePrice,
				book.bookSalePercent,
				image.imageUrl))
			.from(book)
			.leftJoin(bookImage).on(book.eq(bookImage.book))  // book 을 기준으로 bookImage 와 조인
			.orderBy(book.bookPublishDate.desc())  // 출간일 기준 최신순 정렬
			.limit(10)  // 최신 10권으로 결과 제한
			.fetch();
	}

	@Override
	public List<GetProductSimpleResponse> getRandomBooks(int limit) {
		return queryFactory
			.select(Projections.constructor(
				GetProductSimpleResponse.class,
				book.bookId,
				book.author.authorName,
				book.bookTitle,
				book.bookPrice,
				book.bookSalePrice,
				book.bookSalePercent,
				image.imageUrl))
			.from(book)
			.leftJoin(bookImage).on(book.eq(bookImage.book))
			.orderBy(Expressions.stringTemplate("function('RAND')").asc())
			.limit(limit)
			.fetch();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Page<GetProductSimpleResponse> getBooksByCategory(Pageable pageable, String categoryName) {
		List<GetProductSimpleResponse> result = queryFactory
			.select(Projections.constructor(
				GetProductSimpleResponse.class,
				book.bookId,
				book.author.authorName,
				book.bookTitle,
				book.bookPrice,
				book.bookSalePrice,
				book.bookSalePercent,
				image.imageUrl))
			.from(book)
			.leftJoin(bookImage).on(book.eq(bookImage.book))  // book 을 기준으로 bookImage 와 조인
			.join(bookCategory).on(book.eq(bookCategory.book))  // book 을 기준으로 bookCategory 와 조인
			.join(category).on(bookCategory.category.eq(category))  // bookCategory 의 category 와 category 조인
			.where(category.categoryName.eq(categoryName))
			.offset(pageable.getPageNumber()) // 페이지 시작점
			.limit(pageable.getPageSize()) // 페이지 크기
			.orderBy(SortUtil.getSort(pageable, book))
			.fetch();

		Long total = Optional.ofNullable(queryFactory
			.select(book.count())
			.from(book)
			.join(bookCategory).on(bookCategory.book.eq(book))
			.join(bookCategory.category, category)
			.where(category.categoryName.eq(categoryName))
			.fetchOne()).orElse(0L);

		return new PageImpl<>(result, pageable, total);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<GetProductResponse> getProduct(Long bookId) {

		// 상품의 리뷰 평점
		Double reviewScoreAvg = queryFactory
			.select(review.reviewScore.avg())
			.from(review)
			.join(review.bookOrder, bookOrder)
			.where(bookOrder.book.bookId.eq(bookId))
			.fetchOne();

		reviewScoreAvg = reviewScoreAvg != null ? reviewScoreAvg : 0.0;

		// 서브쿼리: 카테고리 리스트
		List<String> categories = queryFactory
			.select(category.categoryName)
			.from(bookCategory)
			.join(bookCategory.category, category)
			.where(bookCategory.book.bookId.eq(bookId))
			.fetch();

		// 서브쿼리: 태그 리스트
		List<String> tags = queryFactory
			.select(tag.tagName)
			.from(bookTag)
			.join(bookTag.tag, tag)
			.where(bookTag.book.bookId.eq(bookId))
			.fetch();

		// 메인 쿼리: 나머지 필드들
		GetProductResponse result = queryFactory
			.select(Projections.constructor(
				GetProductResponse.class,
				book.bookId,
				book.author.authorName,
				book.publisher.publisherName,
				book.bookStatus.bookStatusName,
				book.bookTitle,
				book.bookDescription,
				book.bookQuantity,
				book.bookPublishDate,
				book.bookIsbn,
				book.bookPrice,
				book.bookSalePrice,
				book.bookSalePercent,
				image.imageUrl,
				Expressions.constant(categories),  // 서브쿼리 결과 전달
				Expressions.constant(tags),  // 서브쿼리 결과 전달
				Expressions.constant(reviewScoreAvg)
			))
			.from(book)
			.leftJoin(bookImage).on(book.eq(bookImage.book))
			.where(book.bookId.eq(bookId))
			.fetchOne();

		return Optional.ofNullable(result);
	}

}



