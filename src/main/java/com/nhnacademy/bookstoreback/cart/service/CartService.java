package com.nhnacademy.bookstoreback.cart.service;

import com.nhnacademy.bookstoreback.cart.domain.entity.Cart;

public interface CartService {
	Cart createCart(Cart cart);

	Cart getCart(Long cardId);
}
