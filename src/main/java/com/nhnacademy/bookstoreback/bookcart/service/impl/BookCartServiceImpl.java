package com.nhnacademy.bookstoreback.bookcart.service.impl;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nhnacademy.bookstoreback.auth.jwt.dto.CurrentUserDetails;
import com.nhnacademy.bookstoreback.book.domain.entity.Book;
import com.nhnacademy.bookstoreback.book.exception.BookAlreadyExistsException;
import com.nhnacademy.bookstoreback.book.exception.BookNotFoundException;
import com.nhnacademy.bookstoreback.book.repository.BookRepository;
import com.nhnacademy.bookstoreback.bookcart.domain.dto.request.CreateBookCartRequest;
import com.nhnacademy.bookstoreback.bookcart.domain.dto.request.UpdateBookCartRequest;
import com.nhnacademy.bookstoreback.bookcart.domain.dto.response.GetBookCartResponse;
import com.nhnacademy.bookstoreback.bookcart.domain.entity.BookCart;
import com.nhnacademy.bookstoreback.bookcart.exception.BookCartNotFoundException;
import com.nhnacademy.bookstoreback.bookcart.repository.BookCartRepository;
import com.nhnacademy.bookstoreback.bookcart.service.BookCartService;
import com.nhnacademy.bookstoreback.cart.domain.entity.Cart;
import com.nhnacademy.bookstoreback.cart.exception.UserCartNotFoundException;
import com.nhnacademy.bookstoreback.cart.repository.CartRepository;
import com.nhnacademy.bookstoreback.global.util.CookieUtil;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class BookCartServiceImpl implements BookCartService {
	private final BookCartRepository bookCartRepository;
	private final BookRepository bookRepository;
	private final CartRepository cartRepository;

	@Transactional(readOnly = true)
	@Override
	public List<GetBookCartResponse> getBookCartsByCartId(CurrentUserDetails currentUser, HttpServletRequest req) {
		Long userId = currentUser != null ? currentUser.getUserId() : null;
		// 비회원일 경우
		if (userId == null) {
			Long cartId = Long.valueOf(Objects.requireNonNull(CookieUtil.getCookie(req, "cartId")).getValue());
			return bookCartRepository.findAllByCartCartId(cartId)
				.stream().map(GetBookCartResponse::fromEntity).toList();
		}
		//회원일 경우
		Long cartId = cartRepository.findByUserId(userId)
			.orElseThrow(() -> new UserCartNotFoundException(userId)).getCartId();
		return bookCartRepository.findAllByCartCartId(cartId).stream().map(GetBookCartResponse::fromEntity).toList();
	}

	@Override
	public void createBookCart(CurrentUserDetails currentUser, HttpServletRequest req, CreateBookCartRequest request) {
		Long userId = currentUser != null ? currentUser.getUserId() : null;
		Cart cart = null;
		// 비회원일 경우
		if (userId == null) {
			Long cartId = Long.valueOf(Objects.requireNonNull(CookieUtil.getCookie(req, "cartId")).getValue());
			cart = cartRepository.findById(cartId)
				.orElseThrow(() -> new UserCartNotFoundException("비회원"));
		} else {
			// 회원일 경우
			cart = cartRepository.findByUserId(userId).orElseThrow(() -> new UserCartNotFoundException(userId));
		}

		Book book = bookRepository.findById(request.bookId())
			.orElseThrow(() -> new BookNotFoundException(request.bookId()));

		if (bookCartRepository.existsByCartCartIdAndBookBookId(cart.getCartId(), request.bookId())) {
			throw new BookAlreadyExistsException(request.bookId());
		}

		BookCart bookCart = new BookCart(book, cart, request.bookQuantity());

		bookCartRepository.save(bookCart);
	}

	@Override
	public void updateBookCart(Long bookCartId, CurrentUserDetails currentUser, UpdateBookCartRequest request,
		HttpServletRequest req) {
		Long userId = currentUser != null ? currentUser.getUserId() : null;
		// 비회원일 경우
		if (userId == null) {
			Long cartId = Long.valueOf(Objects.requireNonNull(CookieUtil.getCookie(req, "cartId")).getValue());
			cartRepository.findById(cartId).orElseThrow(() -> new UserCartNotFoundException("비회원"));
		} else {
			// 회원일 경우
			cartRepository.findByUserId(userId).orElseThrow(() -> new UserCartNotFoundException(userId));
		}

		bookCartRepository.findById(bookCartId).orElseThrow(() -> new BookCartNotFoundException(bookCartId));

		BookCart bookCart = bookCartRepository.findById(bookCartId)
			.orElseThrow(() -> new BookCartNotFoundException(bookCartId));

		bookCart.updateBookQuantity(request.bookQuantity());
	}

	@Override
	public void deleteBookCart(Long bookCartId, CurrentUserDetails currentUser, HttpServletRequest req) {
		Long userId = currentUser != null ? currentUser.getUserId() : null;
		// 비회원일 경우
		if (userId == null) {
			Long cartId = Long.valueOf(Objects.requireNonNull(CookieUtil.getCookie(req, "cartId")).getValue());
			cartRepository.findById(cartId).orElseThrow(() -> new UserCartNotFoundException("비회원"));
		} else {
			// 회원일 경우
			cartRepository.findByUserId(userId).orElseThrow(() -> new UserCartNotFoundException(userId));
		}

		bookCartRepository.findById(bookCartId).orElseThrow(() -> new BookCartNotFoundException(bookCartId));
		bookCartRepository.deleteById(bookCartId);
	}

}
