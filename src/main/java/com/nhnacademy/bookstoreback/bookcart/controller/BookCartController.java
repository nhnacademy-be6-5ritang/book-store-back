package com.nhnacademy.bookstoreback.bookcart.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nhnacademy.bookstoreback.auth.annotation.CurrentUser;
import com.nhnacademy.bookstoreback.auth.jwt.dto.CurrentUserDetails;
import com.nhnacademy.bookstoreback.bookcart.domain.dto.request.CreateBookCartRequest;
import com.nhnacademy.bookstoreback.bookcart.domain.dto.request.UpdateBookCartRequest;
import com.nhnacademy.bookstoreback.bookcart.domain.dto.response.GetBookCartResponse;
import com.nhnacademy.bookstoreback.bookcart.service.BookCartService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/carts/me")
public class BookCartController {
	private final BookCartService bookCartService;

	@GetMapping
	public ResponseEntity<List<GetBookCartResponse>> getBookCarts(
		@CurrentUser CurrentUserDetails currentUser, @CookieValue(name = "cartId", required = false) String cartId) {
		return ResponseEntity.status(HttpStatus.OK)
			.body(bookCartService.getBookCartsByCartId(currentUser, cartId));
	}

	@PostMapping
	public ResponseEntity<Void> createBookCart(@CurrentUser CurrentUserDetails currentUser,
		@RequestBody CreateBookCartRequest request, @CookieValue(name = "cartId", required = false) String cartId) {
		bookCartService.createBookCart(currentUser, request, cartId);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@PutMapping("/{bookId}")
	public ResponseEntity<Void> updateBookCart(@CurrentUser CurrentUserDetails currentUser,
		@PathVariable Long bookId, @RequestBody UpdateBookCartRequest request,
		@CookieValue(name = "cartId", required = false) String cartId) {
		bookCartService.updateBookCart(bookId, currentUser, request, cartId);
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@DeleteMapping("/{bookId}")
	public ResponseEntity<Void> deleteBookCart(@CurrentUser CurrentUserDetails currentUser,
		@PathVariable Long bookId, @CookieValue(name = "cartId", required = false) String cartId) {
		bookCartService.deleteBookCart(bookId, currentUser, cartId);
		return ResponseEntity.noContent().build();
	}
}
