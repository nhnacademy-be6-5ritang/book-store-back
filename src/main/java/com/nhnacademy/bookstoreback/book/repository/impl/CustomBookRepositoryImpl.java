package com.nhnacademy.bookstoreback.book.repository.impl;

import static com.nhnacademy.bookstoreback.book.domain.entity.QBook.*;
import static com.nhnacademy.bookstoreback.book.domain.entity.QBookImage.*;
import static com.nhnacademy.bookstoreback.category.domain.entity.QBookCategory.*;
import static com.nhnacademy.bookstoreback.category.domain.entity.QCategory.*;
import static com.nhnacademy.bookstoreback.image.domain.entity.QImage.*;
import static com.nhnacademy.bookstoreback.tag.domain.entity.QBookTag.*;
import static com.nhnacademy.bookstoreback.tag.domain.entity.QTag.*;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.nhnacademy.bookstoreback.book.domain.dto.response.BookSearchResult;
import com.nhnacademy.bookstoreback.book.domain.dto.response.GetBookResponse;
import com.nhnacademy.bookstoreback.book.domain.dto.response.GetBookTitleResponse;
import com.nhnacademy.bookstoreback.book.repository.CustomBookRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomBookRepositoryImpl implements CustomBookRepository {
	private final JPAQueryFactory queryFactory;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<GetBookResponse> getBook(Long bookId) {
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
		GetBookResponse result = queryFactory
			.select(Projections.constructor(
				GetBookResponse.class,
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
				Expressions.constant(tags)  // 서브쿼리 결과 전달
			))
			.from(book)
			.leftJoin(bookImage).on(book.eq(bookImage.book))
			.where(book.bookId.eq(bookId))
			.fetchOne();

		return Optional.ofNullable(result);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Page<GetBookTitleResponse> getBooks(Pageable pageable) {
		List<GetBookTitleResponse> result = queryFactory
			.select(Projections.constructor(
				GetBookTitleResponse.class,
				book.bookId,
				book.bookTitle))
			.from(book)
			.offset(pageable.getOffset()) // 페이지 시작점
			.limit(pageable.getPageSize()) // 페이지 크기
			.fetch();

		Long total = Optional.ofNullable(queryFactory
			.select(book.count())
			.from(book)
			.fetchOne()).orElse(0L);

		return new PageImpl<>(result, pageable, total);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<BookSearchResult> findByBookTitleContainingIgnoreCaseCustom(String title) {
		return queryFactory
			.select(book.bookId, book.bookTitle)
			.from(book)
			.where(book.bookTitle.toLowerCase().contains(title.toLowerCase()))
			.fetch()
			.stream()
			.map(b -> new BookSearchResult(b.get(book.bookId), b.get(book.bookTitle)))
			.toList();
	}
}



