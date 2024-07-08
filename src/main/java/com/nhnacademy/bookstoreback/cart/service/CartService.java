package com.nhnacademy.bookstoreback.cart.service;

import com.nhnacademy.bookstoreback.cart.domain.dto.response.GetCartResponse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface CartService {
	GetCartResponse getCart(Long userId, HttpServletRequest req);

	void createCart(Long userId, HttpServletResponse response);

}
