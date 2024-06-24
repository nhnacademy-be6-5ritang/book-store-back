package com.nhnacademy.bookstoreback.cart.service;

import com.nhnacademy.bookstoreback.cart.domain.dto.response.GetCartResponse;
import com.nhnacademy.bookstoreback.cart.domain.entity.Cart;

public interface CartService {
	Cart createCart(Cart cart);

	GetCartResponse getCart(Long cardId);
}
