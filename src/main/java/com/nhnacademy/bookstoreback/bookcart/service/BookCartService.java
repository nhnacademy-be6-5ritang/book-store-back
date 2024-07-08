package com.nhnacademy.bookstoreback.bookcart.service;

import java.util.List;

import com.nhnacademy.bookstoreback.auth.jwt.dto.CurrentUserDetails;
import com.nhnacademy.bookstoreback.bookcart.domain.dto.request.CreateBookCartRequest;
import com.nhnacademy.bookstoreback.bookcart.domain.dto.request.UpdateBookCartRequest;
import com.nhnacademy.bookstoreback.bookcart.domain.dto.response.GetBookCartResponse;

import jakarta.servlet.http.HttpServletRequest;

public interface BookCartService {
	List<GetBookCartResponse> getBookCartsByCartId(CurrentUserDetails currentUser, HttpServletRequest request);

	void createBookCart(CurrentUserDetails currentUser, HttpServletRequest req, CreateBookCartRequest request);

	void updateBookCart(Long bookCartId, CurrentUserDetails currentUser, UpdateBookCartRequest request,
		HttpServletRequest req);

	void deleteBookCart(Long bookCartId, CurrentUserDetails currentUser, HttpServletRequest req);
}
