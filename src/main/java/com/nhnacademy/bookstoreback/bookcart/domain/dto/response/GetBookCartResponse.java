package com.nhnacademy.bookstoreback.bookcart.domain.dto.response;

import lombok.Builder;

@Builder
public record GetBookCartResponse(Long bookId, int bookQuantity) {
}
