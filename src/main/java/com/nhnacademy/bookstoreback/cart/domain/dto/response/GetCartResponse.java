package com.nhnacademy.bookstoreback.cart.domain.dto.response;

import com.nhnacademy.bookstoreback.cart.domain.entity.Cart;

import lombok.Builder;

@Builder
public record GetCartResponse(
	String cardId,
	Long userId) {

	public static GetCartResponse fromEntity(Cart cart) {
		Long userId = cart.getUserId() != null ? cart.getUserId() : null;
		return GetCartResponse.builder()
			.cardId(cart.getCartId())
			.userId(userId)
			.build();
	}
}
