package com.nhnacademy.bookstoreback.wishlist.domain.dto.response;

import lombok.Builder;

@Builder
public record GetWishListResponse(Long bookId) {
}
