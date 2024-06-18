package com.nhnacademy.bookstoreback.bookcart.domain.dto.request;

import lombok.Builder;

@Builder
public record DeleteBookCartRequest(Long bookCartId) {
}
