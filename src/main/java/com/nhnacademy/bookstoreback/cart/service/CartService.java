package com.nhnacademy.bookstoreback.cart.service;

import com.nhnacademy.bookstoreback.cart.domain.dto.request.CreateCartRequest;
import com.nhnacademy.bookstoreback.cart.domain.dto.request.GetCartRequest;
import com.nhnacademy.bookstoreback.cart.domain.dto.response.CreateCartResponse;
import com.nhnacademy.bookstoreback.cart.domain.dto.response.GetCartResponse;

public interface CartService {
	GetCartResponse getCart(Long userId, GetCartRequest request);

	CreateCartResponse createCart(Long userId, CreateCartRequest request);

}
