package com.nhnacademy.bookstoreback.wishlist.domain.dto.response;

import lombok.Builder;

@Builder
public record CreateWishListResponse(long bookId, long userId) {
}
