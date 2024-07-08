package com.nhnacademy.bookstoreback.cart.domain.dto.response;

import com.nhnacademy.bookstoreback.cart.domain.entity.Cart;

import lombok.Builder;

@Builder
public record GetCartResponse(
	Long cardId,
	Long userId) {

	public static GetCartResponse fromEntity(Cart cart) {
		return GetCartResponse.builder()
			.cardId(cart.getCartId())
			.userId(cart.getCartId())
			.build();
	}
}
