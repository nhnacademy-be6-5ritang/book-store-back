package com.nhnacademy.bookstoreback.cart.domain.dto.request;

import lombok.Builder;

@Builder
public record GetCartRequest(Long userid) {
}
