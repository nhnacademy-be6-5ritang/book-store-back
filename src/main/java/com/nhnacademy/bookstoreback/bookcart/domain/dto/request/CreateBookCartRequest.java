package com.nhnacademy.bookstoreback.bookcart.domain.dto.request;

public record CreateBookCartRequest(
	Long bookId,
	Integer bookQuantity) {
}
