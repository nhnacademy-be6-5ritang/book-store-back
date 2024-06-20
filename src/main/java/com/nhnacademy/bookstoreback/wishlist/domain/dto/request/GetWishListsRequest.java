package com.nhnacademy.bookstoreback.wishlist.domain.dto.request;

import lombok.Builder;

@Builder
public record GetWishListsRequest(Long userId) {
}
