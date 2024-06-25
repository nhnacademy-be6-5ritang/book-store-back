package com.nhnacademy.bookstoreback.cart.service;

import com.nhnacademy.bookstoreback.cart.domain.dto.response.CreateCartResponse;
import com.nhnacademy.bookstoreback.cart.domain.dto.response.GetCartResponse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface CartService {
	CreateCartResponse createCart(HttpServletRequest req, HttpServletResponse resp, Long userId);

	GetCartResponse getCart(Long cardId);
}
