package com.nhnacademy.bookstoreback.bookcart.service;

import com.nhnacademy.bookstoreback.bookcart.domain.entity.BookCart;

public interface BookCartService {
	BookCart createBookCart(BookCart bookCart);

	BookCart updateBookCart(Long bookCartId, BookCart bookCart);

	void deleteBookCart(Long bookCartId);
}
