package com.nhnacademy.bookstoreback.bookcart.domain.dto.request;

import lombok.Builder;

@Builder
public record UpdateBookCartRequest(int bookQuantity, Long bookId, Long cartId) {
}
