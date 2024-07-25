package com.nhnacademy.bookstoreback.review.domain.dto.response;

import com.nhnacademy.bookstoreback.order.domain.entity.BookOrder;

import lombok.Builder;

/**
 * 리뷰 작성 가능 도서 응답 DTO
 *
 * @author 이경헌
 * @version 1.0
 */
@Builder
public record GetBookOrderWithoutReviewResponse(
	Long orderListId,
	String bookTitle) {

	public static GetBookOrderWithoutReviewResponse fromEntity(BookOrder bookOrder) {
		return GetBookOrderWithoutReviewResponse.builder()
			.orderListId(bookOrder.getOrderListId())
			.bookTitle(bookOrder.getBook().getBookTitle())
			.build();
	}
}
