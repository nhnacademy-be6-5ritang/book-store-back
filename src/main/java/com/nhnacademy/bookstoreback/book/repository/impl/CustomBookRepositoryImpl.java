package com.nhnacademy.bookstoreback.book.repository.impl;

import static com.nhnacademy.bookstoreback.book.domain.entity.QBook.*;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.nhnacademy.bookstoreback.book.domain.dto.response.BookSearchResult;
import com.nhnacademy.bookstoreback.book.domain.dto.response.GetBookTitleResponse;
import com.nhnacademy.bookstoreback.book.domain.entity.QBook;
import com.nhnacademy.bookstoreback.book.repository.CustomBookRepository;
import com.nhnacademy.bookstoreback.order.domain.entity.QBookOrder;
import com.nhnacademy.bookstoreback.order.domain.entity.QOrder;
import com.nhnacademy.bookstoreback.order.domain.entity.QOrderStatus;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;

@Repository
public class CustomBookRepositoryImpl implements CustomBookRepository {

	private final JPAQueryFactory queryFactory;

	public CustomBookRepositoryImpl(EntityManager em) {
		this.queryFactory = new JPAQueryFactory(em);
	}

	@Override
	public List<BookSearchResult> findByBookTitleContainingIgnoreCaseCustom(String title) {
		return queryFactory
			.select(book)
			.from(book)
			.where(book.bookTitle.toLowerCase().contains(title.toLowerCase()))
			.fetch()
			.stream()
			.map(b -> new BookSearchResult(b.getBookId(), b.getBookTitle()))
			.collect(Collectors.toList());
	}

	@Override
	public List<GetBookTitleResponse> getBooksByOrderStatusCompletionAndUserId(String orderStatusName, Long userId) {
		QOrder qOrder = QOrder.order;
		QBookOrder qBookOrder = QBookOrder.bookOrder;
		QBook qBook = QBook.book;
		QOrderStatus qOrderStatus = QOrderStatus.orderStatus;

		return queryFactory
			.select(book)
			.from(qOrder)
			.join(qBookOrder).on(qOrder.orderId.eq(qBookOrder.order.orderId))
			.join(qBook).on(qBookOrder.book.bookId.eq(qBook.bookId))
			.join(qOrder.orderStatus, qOrderStatus)
			.where(qOrderStatus.orderStatusName.eq(orderStatusName)
				.and(qOrder.user.id.eq(userId)))
			.fetch()
			.stream()
			.map(b -> GetBookTitleResponse.builder()
				.bookId(b.getBookId())
				.bookTitle(b.getBookTitle())
				.build())
			.toList();
	}

}