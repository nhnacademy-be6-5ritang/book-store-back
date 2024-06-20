package com.nhnacademy.bookstoreback.cart.domain.dto.response;

import lombok.Builder;

@Builder
public record GetCartResponse(String cartId, Long userId) {
}
