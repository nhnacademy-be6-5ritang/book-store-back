package com.nhnacademy.bookstoreback.wishlist.domain.dto.response;

import java.util.List;

import com.nhnacademy.bookstoreback.wishlist.domain.entity.WishList;

import lombok.Builder;

@Builder
public record GetWishListResponse(Long bookId) {
}
