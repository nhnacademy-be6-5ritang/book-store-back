package com.nhnacademy.bookstoreback.cart.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nhnacademy.bookstoreback.auth.annotation.CurrentUser;
import com.nhnacademy.bookstoreback.auth.jwt.dto.CurrentUserDetails;
import com.nhnacademy.bookstoreback.cart.domain.dto.response.GetCartResponse;
import com.nhnacademy.bookstoreback.cart.service.CartService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/carts")
public class CartController {
	private final CartService cartService;

	@GetMapping
	public ResponseEntity<GetCartResponse> getCart(@CurrentUser CurrentUserDetails currentUser,
		HttpServletRequest req) {
		return ResponseEntity.ok(cartService.getCart(currentUser.getUserId(), req));
	}

	@PostMapping
	public ResponseEntity<Void> createCart(@CurrentUser CurrentUserDetails currentUser,
		HttpServletResponse response) {
		cartService.createCart(currentUser.getUserId(), response);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
}
