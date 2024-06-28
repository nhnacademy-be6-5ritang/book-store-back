package com.nhnacademy.bookstoreback.cart.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nhnacademy.bookstoreback.cart.domain.dto.response.CreateCartResponse;
import com.nhnacademy.bookstoreback.cart.domain.dto.response.GetCartResponse;
import com.nhnacademy.bookstoreback.cart.service.CartService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/back/carts")
public class CartController {
	private final CartService cartService;

	@PostMapping()
	public ResponseEntity<CreateCartResponse> createCart(HttpServletRequest req, HttpServletResponse resp) {
		Long userId = null;

		CreateCartResponse response = cartService.createCart(req, resp, userId);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@GetMapping("/{cartId}")
	public ResponseEntity<GetCartResponse> getCart(@PathVariable Long cartId) {
		GetCartResponse cart = cartService.getCart(cartId);
		return ResponseEntity.ok(cart);
	}
}
