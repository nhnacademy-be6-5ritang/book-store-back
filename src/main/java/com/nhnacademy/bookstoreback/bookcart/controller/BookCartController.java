package com.nhnacademy.bookstoreback.bookcart.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nhnacademy.bookstoreback.bookcart.domain.dto.request.CreateBookCartRequest;
import com.nhnacademy.bookstoreback.bookcart.domain.dto.request.UpdateBookCartRequest;
import com.nhnacademy.bookstoreback.bookcart.domain.dto.response.CreateBookCartResponse;
import com.nhnacademy.bookstoreback.bookcart.domain.dto.response.GetBookCartResponse;
import com.nhnacademy.bookstoreback.bookcart.domain.dto.response.UpdateBookCartResponse;
import com.nhnacademy.bookstoreback.bookcart.service.BookCartService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/carts/{cartId}/books-carts")
public class BookCartController {
	private final BookCartService bookCartService;

	@PostMapping
	public ResponseEntity<CreateBookCartResponse> createBookCart(@RequestBody CreateBookCartRequest request) {
		CreateBookCartResponse response = bookCartService.createBookCart(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@GetMapping
	public ResponseEntity<List<GetBookCartResponse>> getAllBookCarts(@PathVariable("cartId") Long cartId) {
		List<GetBookCartResponse> bookCarts = bookCartService.getBookCarts(cartId);
		return ResponseEntity.status(HttpStatus.OK).body(bookCarts);
	}

	@PutMapping("/{bookCartId}")
	public ResponseEntity<UpdateBookCartResponse> updateBookCart(@PathVariable Long bookCartId,
		@RequestBody UpdateBookCartRequest request) {
		UpdateBookCartResponse response = bookCartService.updateBookCart(bookCartId, request);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@DeleteMapping("/{bookCartId}")
	public ResponseEntity<Void> deleteBookCart(@PathVariable Long bookCartId) {
		bookCartService.deleteBookCart(bookCartId);
		return ResponseEntity.noContent().build();
	}
}
