package com.nhnacademy.bookstoreback.order.repository.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import com.nhnacademy.bookstoreback.book.domain.entity.QBook;
import com.nhnacademy.bookstoreback.category.domain.entity.QBookCategory;
import com.nhnacademy.bookstoreback.order.domain.dto.response.GetBookByOrderCouponResponse;
import com.nhnacademy.bookstoreback.order.domain.entity.QBookOrder;
import com.nhnacademy.bookstoreback.order.repository.CustomBookOrderRepository;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;

public class CustomBookOrderRepositoryImpl implements CustomBookOrderRepository {

	private final JPAQueryFactory queryFactory;

	public CustomBookOrderRepositoryImpl(JPAQueryFactory queryFactory) {
		this.queryFactory = queryFactory;
	}

	@Override
	public GetBookByOrderCouponResponse findBooksByOrderListId(Long orderListId) {
		QBookOrder bookOrder = QBookOrder.bookOrder;
		QBookCategory bookCategory = QBookCategory.bookCategory;
		QBook book = QBook.book;

		// Fetch the results as tuples
		List<Tuple> results = queryFactory
			.select(bookOrder.book.bookId, bookCategory.category.categoryId, book.bookSalePrice)
			.from(bookOrder)
			.join(bookCategory).on(bookOrder.book.bookId.eq(bookCategory.book.bookId))
			.join(book).on(bookOrder.book.bookId.eq(book.bookId))
			.where(bookOrder.orderListId.eq(orderListId))
			.fetch();

		// Group by bookId and collect categoryIds into a list
		Map<Long, GetBookByOrderCouponResponse> groupedResults = results.stream()
			.filter(tuple -> tuple.get(bookOrder.book.bookId) != null) // Filter out null bookIds
			.collect(Collectors.groupingBy(
				tuple -> tuple.get(bookOrder.book.bookId),
				Collectors.collectingAndThen(
					Collectors.toList(),
					list -> {
						List<Long> categoryIds = list.stream()
							.map(tuple -> tuple.get(bookCategory.category.categoryId))
							.filter(Objects::nonNull) // Filter out null categoryIds
							.collect(Collectors.toList());
						BigDecimal salePrice = list.getFirst()
							.get(book.bookSalePrice); // Assuming sale price is the same for all tuples
						return new GetBookByOrderCouponResponse(list.getFirst().get(bookOrder.book.bookId), salePrice,
							categoryIds);
					}
				)
			));

		// Return the first element of the map
		return groupedResults.values().stream()
			.findFirst()
			.orElse(null); // Or handle the case where no valid bookId is found
	}
}