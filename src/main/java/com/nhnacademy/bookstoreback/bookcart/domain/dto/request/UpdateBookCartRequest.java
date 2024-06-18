package com.nhnacademy.bookstoreback.bookcart.domain.dto.request;

import lombok.Builder;

@Builder
public record UpdateBookCartRequest(Long bookCartId, int bookQuantity) {
}
