package com.nhnacademy.bookstoreback.bookcart.service;

import java.util.List;

import com.nhnacademy.bookstoreback.bookcart.domain.entity.BookCart;

public interface BookCartService {
	BookCart createBookCart(BookCart bookCart);

	List<BookCart> getBookCarts(Long cartId);

	BookCart updateBookCart(Long bookCartId, BookCart bookCart);

	void deleteBookCart(Long bookCartId);
}
