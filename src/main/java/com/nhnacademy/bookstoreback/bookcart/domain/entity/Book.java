package com.nhnacademy.bookstoreback.bookcart.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Book {
	private Long bookId;
	private Long bookQuantity;

	public void updateBookQuantity(Long bookQuantity) {
		this.bookQuantity = bookQuantity;
	}
}
