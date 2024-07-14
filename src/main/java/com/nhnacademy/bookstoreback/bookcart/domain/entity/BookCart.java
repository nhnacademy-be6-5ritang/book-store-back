package com.nhnacademy.bookstoreback.bookcart.domain.entity;

import org.springframework.data.annotation.Id;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookCart {
	@Id
	private String cartId;
	private Long bookId;
	private int bookQuantity;

	public BookCart(String cartId) {
		this.cartId = cartId;
	}

	public BookCart(Long bookId, int bookQuantity) {
		this.bookId = bookId;
		this.bookQuantity = bookQuantity;
	}

	public BookCart(String cartId, Long bookId, int bookQuantity) {
		this.cartId = cartId;
		this.bookId = bookId;
		this.bookQuantity = bookQuantity;
	}

	public void updateBookQuantity(int bookQuantity) {
		this.bookQuantity = bookQuantity;
	}
}
