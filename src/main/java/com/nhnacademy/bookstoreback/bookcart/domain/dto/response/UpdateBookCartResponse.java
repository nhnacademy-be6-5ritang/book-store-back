package com.nhnacademy.bookstoreback.bookcart.domain.dto.response;

import lombok.Builder;

@Builder
public record UpdateBookCartResponse(Long bookCartId, int bookQuantity, Long bookId, Long cartId) {
}
