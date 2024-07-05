package com.nhnacademy.bookstoreback.cart.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nhnacademy.bookstoreback.auth.annotation.CurrentUser;
import com.nhnacademy.bookstoreback.auth.jwt.dto.CurrentUserDetails;
import com.nhnacademy.bookstoreback.cart.domain.dto.request.CreateCartRequest;
import com.nhnacademy.bookstoreback.cart.domain.dto.request.GetCartRequest;
import com.nhnacademy.bookstoreback.cart.domain.dto.response.CreateCartResponse;
import com.nhnacademy.bookstoreback.cart.domain.dto.response.GetCartResponse;
import com.nhnacademy.bookstoreback.cart.service.CartService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/carts")
public class CartController {
	private final CartService cartService;

	@GetMapping
	public ResponseEntity<GetCartResponse> getCart(@CurrentUser CurrentUserDetails currentUserDetails,
		GetCartRequest request) {
		Long userId = currentUserDetails.getUserId();
		GetCartResponse cart = cartService.getCart(userId, request);
		return ResponseEntity.ok(cart);
	}

	@PostMapping
	public ResponseEntity<CreateCartResponse> createCart(@CurrentUser CurrentUserDetails currentUserDetails,
		CreateCartRequest request) {
		Long userId = currentUserDetails.getUserId();

		CreateCartResponse response = cartService.createCart(userId, request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
}
