package com.nhnacademy.bookstoreback.cart.domain.dto.response;

import com.nhnacademy.bookstoreback.cart.domain.entity.Cart;

import lombok.Builder;

@Builder
public record GetCartResponse(
	Long cardId,
	Long userId) {

	public static GetCartResponse fromEntity(Cart cart) {
		Long userId = cart.getUser() != null ? cart.getUser().getId() : null;
		return GetCartResponse.builder()
			.cardId(cart.getCartId())
			.userId(userId)
			.build();
	}
}
