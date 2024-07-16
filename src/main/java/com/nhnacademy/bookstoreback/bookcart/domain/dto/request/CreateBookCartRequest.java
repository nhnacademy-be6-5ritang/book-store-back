package com.nhnacademy.bookstoreback.bookcart.domain.dto.request;

import jakarta.validation.constraints.NotNull;

public record CreateBookCartRequest(
	@NotNull Long bookId,
	@NotNull Integer bookQuantity) {
}
