package com.nhnacademy.bookstoreback.bookcart.service;

import java.util.List;

import com.nhnacademy.bookstoreback.bookcart.domain.dto.request.CreateBookCartRequest;
import com.nhnacademy.bookstoreback.bookcart.domain.dto.request.UpdateBookCartRequest;
import com.nhnacademy.bookstoreback.bookcart.domain.dto.response.CreateBookCartResponse;
import com.nhnacademy.bookstoreback.bookcart.domain.dto.response.GetBookCartResponse;
import com.nhnacademy.bookstoreback.bookcart.domain.dto.response.UpdateBookCartResponse;

public interface BookCartService {
	CreateBookCartResponse createBookCart(CreateBookCartRequest request);

	List<GetBookCartResponse> getBookCarts(Long cartId);

	UpdateBookCartResponse updateBookCart(Long bookCartId, UpdateBookCartRequest request);

	void deleteBookCart(Long bookCartId);
}
