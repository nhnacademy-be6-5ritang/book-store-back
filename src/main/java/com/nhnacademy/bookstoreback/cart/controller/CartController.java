package com.nhnacademy.bookstoreback.cart.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nhnacademy.bookstoreback.cart.domain.dto.CartDto;
import com.nhnacademy.bookstoreback.cart.domain.dto.request.CreateCartRequest;
import com.nhnacademy.bookstoreback.cart.service.CartService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/carts")
public class CartController {
	private final CartService cartService;

	@GetMapping("/{cartId}")
	public ResponseEntity<CartDto> getCart(@PathVariable Long cartId) {
		CartDto cart = cartService.getCart(cartId);
		return ResponseEntity.ok(cart);
	}

	@PostMapping
	public ResponseEntity<CartDto> createCart(CreateCartRequest request) {
		Long userId = null;

		CartDto response = cartService.createCart(userId, request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
}
