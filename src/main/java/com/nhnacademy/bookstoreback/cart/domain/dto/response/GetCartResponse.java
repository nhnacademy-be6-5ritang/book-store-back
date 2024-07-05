package com.nhnacademy.bookstoreback.cart.domain.dto.response;

import com.nhnacademy.bookstoreback.cart.domain.entity.Cart;

import jakarta.servlet.http.Cookie;
import lombok.Builder;

@Builder
public record GetCartResponse(
	Cookie cookie,
	Long cardId,
	Long userId) {

	public static GetCartResponse fromEntity(Cookie cookie, Cart cart) {
		return GetCartResponse.builder()
			.cookie(cookie)
			.cardId(cart.getCartId())
			.userId(cart.getCartId())
			.build();
	}
}
