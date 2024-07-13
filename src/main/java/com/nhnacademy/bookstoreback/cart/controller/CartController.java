package com.nhnacademy.bookstoreback.cart.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nhnacademy.bookstoreback.auth.annotation.CurrentUser;
import com.nhnacademy.bookstoreback.auth.jwt.dto.CurrentUserDetails;
import com.nhnacademy.bookstoreback.cart.domain.dto.response.GetCartResponse;
import com.nhnacademy.bookstoreback.cart.service.CartService;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/carts")
public class CartController {
	private final CartService cartService;

	@GetMapping("/{cartId}")
	public ResponseEntity<GetCartResponse> getCart(@PathVariable String cartId) {
		return ResponseEntity.ok(cartService.getCart(cartId));
	}

	@PostMapping
	public ResponseEntity<Void> createCart(@CurrentUser CurrentUserDetails currentUser,
		HttpServletResponse resp) {
		cartService.createCart(currentUser, resp);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
}
