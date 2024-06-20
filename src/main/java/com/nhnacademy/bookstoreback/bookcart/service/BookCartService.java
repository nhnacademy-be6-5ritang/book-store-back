package com.nhnacademy.bookstoreback.bookcart.service;

import java.util.List;

import com.nhnacademy.bookstoreback.bookcart.domain.dto.request.CreateBookCartRequest;
import com.nhnacademy.bookstoreback.bookcart.domain.dto.response.CreateBookCartResponse;
import com.nhnacademy.bookstoreback.bookcart.domain.dto.response.GetBookCartResponse;
import com.nhnacademy.bookstoreback.bookcart.domain.entity.BookCart;

public interface BookCartService {
	CreateBookCartResponse createBookCart(CreateBookCartRequest request);

	List<GetBookCartResponse> getBookCarts(Long cartId);

	BookCart updateBookCart(Long bookCartId, BookCart bookCart);

	void deleteBookCart(Long bookCartId);
}
