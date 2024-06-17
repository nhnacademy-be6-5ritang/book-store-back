package com.nhnacademy.bookstoreback.bookcart.service.impl;

import org.springframework.stereotype.Service;

import com.nhnacademy.bookstoreback.bookcart.domain.entity.BookCart;
import com.nhnacademy.bookstoreback.bookcart.repository.BookCartRepository;
import com.nhnacademy.bookstoreback.bookcart.service.BookCartService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookCartServiceImpl implements BookCartService {
	private final BookCartRepository bookCartRepository;

	@Override
	public BookCart createBookCart(BookCart bookCart) {
		return bookCartRepository.save(bookCart);
	}

	@Override
	public BookCart updateBookCart(Long bookCartId, BookCart bookCart) {
		return null;
	}

	@Override
	public void deleteBookCart(Long bookCartId) {
		bookCartRepository.deleteById(bookCartId);
	}

}
