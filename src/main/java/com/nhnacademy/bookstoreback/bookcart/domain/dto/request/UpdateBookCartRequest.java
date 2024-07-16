package com.nhnacademy.bookstoreback.bookcart.domain.dto.request;

import jakarta.validation.constraints.NotNull;

public record UpdateBookCartRequest(
	@NotNull Integer bookQuantity) {
}
