package com.nhnacademy.bookstoreback.cart.service;

import com.nhnacademy.bookstoreback.cart.domain.dto.CartDto;
import com.nhnacademy.bookstoreback.cart.domain.dto.request.CreateCartRequest;

public interface CartService {
	CartDto createCart(Long userId, CreateCartRequest request);

	CartDto getCart(Long cardId);
}
