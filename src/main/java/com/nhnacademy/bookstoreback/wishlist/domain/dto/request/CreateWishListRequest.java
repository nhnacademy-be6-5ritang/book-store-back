package com.nhnacademy.bookstoreback.wishlist.domain.dto.request;

import jakarta.validation.constraints.NotNull;

public record CreateWishListRequest(
	@NotNull Long bookId) {
}
