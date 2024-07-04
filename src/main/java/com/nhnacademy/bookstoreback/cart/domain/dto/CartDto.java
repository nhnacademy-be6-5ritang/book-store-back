package com.nhnacademy.bookstoreback.cart.domain.dto;

import com.nhnacademy.bookstoreback.cart.domain.entity.Cart;

import lombok.Builder;

@Builder
public record CartDto(
	Long cardId,
	Long userId) {

	public static CartDto toDto(Cart cart) {
		return CartDto.builder()
			.cardId(cart.getCartId())
			.userId(cart.getUser().getId())
			.build();
	}
}
