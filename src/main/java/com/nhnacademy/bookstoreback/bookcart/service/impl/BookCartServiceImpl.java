package com.nhnacademy.bookstoreback.bookcart.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.nhnacademy.bookstoreback.book.domain.entity.Book;
import com.nhnacademy.bookstoreback.book.repository.BookRepository;
import com.nhnacademy.bookstoreback.bookcart.domain.dto.request.CreateBookCartRequest;
import com.nhnacademy.bookstoreback.bookcart.domain.dto.request.UpdateBookCartRequest;
import com.nhnacademy.bookstoreback.bookcart.domain.dto.response.CreateBookCartResponse;
import com.nhnacademy.bookstoreback.bookcart.domain.dto.response.GetBookCartResponse;
import com.nhnacademy.bookstoreback.bookcart.domain.dto.response.UpdateBookCartResponse;
import com.nhnacademy.bookstoreback.bookcart.domain.entity.BookCart;
import com.nhnacademy.bookstoreback.bookcart.repository.BookCartRepository;
import com.nhnacademy.bookstoreback.bookcart.service.BookCartService;
import com.nhnacademy.bookstoreback.cart.domain.entity.Cart;
import com.nhnacademy.bookstoreback.cart.repository.CartRepository;
import com.nhnacademy.bookstoreback.global.exception.NotFoundException;
import com.nhnacademy.bookstoreback.global.exception.payload.ErrorStatus;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookCartServiceImpl implements BookCartService {
	private final BookCartRepository bookCartRepository;
	private final BookRepository bookRepository;
	private final CartRepository cartRepository;

	@Override
	public CreateBookCartResponse createBookCart(CreateBookCartRequest request) {
		Book book = bookRepository.findById(request.bookId()).orElseThrow(() -> {
			String errorMessage = String.format("해당 도서 '%d'은 존재하지 않는 도서입니다.", request.bookId());
			ErrorStatus errorStatus = ErrorStatus.from(errorMessage, HttpStatus.NOT_FOUND, LocalDateTime.now());
			return new NotFoundException(errorStatus);
		});

		Cart cart = cartRepository.findById(request.cartId()).orElseThrow(() -> {
			String errorMessage = String.format("해당 카트 '%d'은 존재하지 않는 카트입니다.", request.cartId());
			ErrorStatus errorStatus = ErrorStatus.from(errorMessage, HttpStatus.NOT_FOUND, LocalDateTime.now());
			return new NotFoundException(errorStatus);
		});

		BookCart bookCart = new BookCart(book, cart, request.bookQuantity());

		bookCartRepository.save(bookCart);

		return new CreateBookCartResponse(book.getBookId(), bookCart.getBookQuantity());
	}

	@Override
	public List<GetBookCartResponse> getBookCarts(Long cartId) {
		List<BookCart> bookCarts = bookCartRepository.findAllByCartCartId(cartId);
		return bookCarts.stream()
			.map(bookCart -> GetBookCartResponse.builder()
				.bookQuantity(bookCart.getBookQuantity())
				.bookId(bookCart.getBook().getBookId())
				.cartId(bookCart.getCart().getCartId())
				.build())
			.toList();
	}

	@Override
	public UpdateBookCartResponse updateBookCart(Long bookCartId, UpdateBookCartRequest request) {
		BookCart bookCart = bookCartRepository.findById(bookCartId).orElseThrow(() -> {
			String errorMessage = String.format("해당 도서카트 '%d'은 존재하지 않는 도서카트입니다.", bookCartId);
			ErrorStatus errorStatus = ErrorStatus.from(errorMessage, HttpStatus.NOT_FOUND, LocalDateTime.now());
			return new NotFoundException(errorStatus);
		});

		bookRepository.findById(request.bookId()).orElseThrow(() -> {
			String errorMessage = String.format("해당 도서 '%d'은 존재하지 않는 도서입니다.", request.bookId());
			ErrorStatus errorStatus = ErrorStatus.from(errorMessage, HttpStatus.NOT_FOUND, LocalDateTime.now());
			return new NotFoundException(errorStatus);
		});

		cartRepository.findById(request.cartId()).orElseThrow(() -> {
			String errorMessage = String.format("해당 카트 '%d'은 존재하지 않는 카트입니다.", request.cartId());
			ErrorStatus errorStatus = ErrorStatus.from(errorMessage, HttpStatus.NOT_FOUND, LocalDateTime.now());
			return new NotFoundException(errorStatus);
		});

		bookCart.updateBookQuantity(request.bookQuantity());

		bookCartRepository.save(bookCart);

		return UpdateBookCartResponse.builder()
			.bookCartId(bookCart.getBookCartId())
			.bookQuantity(bookCart.getBookQuantity())
			.bookId(bookCart.getBook().getBookId())
			.cartId(bookCart.getCart().getCartId())
			.build();

	}

	@Override
	public void deleteBookCart(Long bookCartId) {
		bookCartRepository.deleteById(bookCartId);
	}

}
