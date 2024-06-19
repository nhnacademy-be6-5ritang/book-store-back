package com.nhnacademy.bookstoreback.bookcart.domain.dto.request;

import lombok.Builder;

@Builder
public record CreateBookCartRequest(Long bookId, Long cartId, int bookQuantity) {
}
