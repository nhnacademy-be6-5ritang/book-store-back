package com.nhnacademy.bookstoreback.cart.service.impl;

import org.springframework.stereotype.Service;

import com.nhnacademy.bookstoreback.cart.repository.CartRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartServiceImpl {
	private final CartRepository cartRepository;
}
