package com.nhnacademy.bookstoreback.cart.domain.dto.response;

import lombok.Builder;

@Builder
public record CreateCartResponse(Long cardId, Long userId) {
}
