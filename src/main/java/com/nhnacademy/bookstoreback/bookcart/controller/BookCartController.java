package com.nhnacademy.bookstoreback.bookcart.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nhnacademy.bookstoreback.bookcart.domain.entity.BookCart;
import com.nhnacademy.bookstoreback.bookcart.service.BookCartService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bookcarts")
public class BookCartController {
	private final BookCartService bookCartService;

	@PostMapping
	public ResponseEntity<BookCart> createBookCart(@RequestBody BookCart bookCart) {
		BookCart bookCartSaved = bookCartService.createBookCart(bookCart);
		return ResponseEntity.status(HttpStatus.CREATED).body(bookCartSaved);
	}

	@PutMapping("/{bookCartId}")
	public ResponseEntity<BookCart> updateBookCart(@PathVariable Long bookCartId, @RequestBody BookCart bookCart) {
		BookCart bookCart1 = bookCartService.updateBookCart(bookCartId, bookCart);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/{bookCartId}")
	public ResponseEntity<Void> deleteBookCart(@PathVariable Long bookCartId) {
		bookCartService.deleteBookCart(bookCartId);
		return ResponseEntity.noContent().build();
	}
}
