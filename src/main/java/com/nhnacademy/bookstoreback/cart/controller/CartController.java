package com.nhnacademy.bookstoreback.cart.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nhnacademy.bookstoreback.cart.domain.entity.Cart;
import com.nhnacademy.bookstoreback.cart.service.CartService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/carts")
public class CartController {
	private final CartService cartService;

	@PutMapping
	public ResponseEntity<Cart> createCart(@RequestBody Cart cart) {
		Cart cart1 = cartService.createCart(cart);
		return ResponseEntity.status(HttpStatus.CREATED).body(cart1);
	}

	@GetMapping
	public ResponseEntity<Cart> getCart(Long cartId) {
		Cart cart = cartService.getCart(cartId);
		return ResponseEntity.ok(cart);
	}
}
