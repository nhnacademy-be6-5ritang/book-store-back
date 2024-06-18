package com.nhnacademy.bookstoreback.bookcart.domain.dto.response;

import java.util.List;

import com.nhnacademy.bookstoreback.bookcart.domain.entity.BookCart;

import lombok.Builder;

@Builder
public record GetBookCartsResponse(List<BookCart> bookCarts) {
}
