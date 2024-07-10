package com.nhnacademy.bookstoreback.bookcart.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nhnacademy.bookstoreback.auth.jwt.dto.CurrentUserDetails;
import com.nhnacademy.bookstoreback.book.domain.entity.Book;
import com.nhnacademy.bookstoreback.book.exception.BookNotFoundException;
import com.nhnacademy.bookstoreback.book.repository.BookRepository;
import com.nhnacademy.bookstoreback.bookcart.domain.dto.request.CreateBookCartRequest;
import com.nhnacademy.bookstoreback.bookcart.domain.dto.request.UpdateBookCartRequest;
import com.nhnacademy.bookstoreback.bookcart.domain.dto.response.GetBookCartResponse;
import com.nhnacademy.bookstoreback.bookcart.domain.entity.BookCart;
import com.nhnacademy.bookstoreback.bookcart.exception.BookCartAlreadyExistsException;
import com.nhnacademy.bookstoreback.bookcart.exception.BookCartNotFoundException;
import com.nhnacademy.bookstoreback.bookcart.repository.BookCartRepository;
import com.nhnacademy.bookstoreback.bookcart.service.BookCartService;
import com.nhnacademy.bookstoreback.cart.domain.entity.Cart;
import com.nhnacademy.bookstoreback.cart.exception.UserCartNotFoundException;
import com.nhnacademy.bookstoreback.cart.repository.CartRepository;
import com.nhnacademy.bookstoreback.cart.service.CartService;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class BookCartServiceImpl implements BookCartService {
	private final BookCartRepository bookCartRepository;
	private final BookRepository bookRepository;
	private final CartRepository cartRepository;
	private final CartService cartService;
	private final HttpServletResponse resp;

	@Override
	public List<GetBookCartResponse> getBookCartsByCartId(CurrentUserDetails currentUser, Long cartId) {
		Cart cart = setupCart(currentUser, cartId);

		return bookCartRepository.findAllByCartCartId(cart.getCartId())
			.stream()
			.map(GetBookCartResponse::fromEntity)
			.toList();
	}

	@Override
	public void createBookCart(CurrentUserDetails currentUser, CreateBookCartRequest request, Long cartId) {
		Cart cart = setupCart(currentUser, cartId);

		Book book = bookRepository.findById(request.bookId())
			.orElseThrow(() -> new BookNotFoundException(request.bookId()));

		if (bookCartRepository.existsByCartCartIdAndBookBookId(cart.getCartId(), request.bookId())) {
			throw new BookCartAlreadyExistsException(request.bookId());
		}

		BookCart bookCart = new BookCart(book, cart, request.bookQuantity());

		bookCartRepository.save(bookCart);
	}

	@Override
	public void updateBookCart(Long bookCartId, CurrentUserDetails currentUser, UpdateBookCartRequest request,
		Long cartId) {
		setupCart(currentUser, cartId);

		bookCartRepository.findById(bookCartId).orElseThrow(() -> new BookCartNotFoundException(bookCartId));

		BookCart bookCart = bookCartRepository.findById(bookCartId)
			.orElseThrow(() -> new BookCartNotFoundException(bookCartId));

		bookCart.updateBookQuantity(request.bookQuantity());
	}

	@Override
	public void deleteBookCart(Long bookCartId, CurrentUserDetails currentUser, Long cartId) {
		cartId = setupCart(currentUser, cartId).getCartId();

		BookCart bookCart = bookCartRepository.findById(bookCartId)
			.orElseThrow(() -> new BookCartNotFoundException(bookCartId));

		// 해당 카트아이디와 실제 카트아이디가 일치할 경우만 삭제되도록
		if (bookCart.getCart().getCartId().equals(cartId)) {
			bookCartRepository.deleteById(bookCartId);
		}

	}

	public Cart setupCart(CurrentUserDetails currentUser, Long cartId) {
		Long userId = currentUser != null ? currentUser.getUserId() : null;

		// 비회원인데 카트가 없는 경우
		if (userId == null && cartId == null) {
			return cartService.createCart(currentUser, resp);
			// 비회원인데 카트가 있는 경우
		} else if (userId == null) {
			return cartRepository.findById(cartId).orElseThrow(() -> new UserCartNotFoundException(cartId));
			// 회원인데 카트가 없는 경우
		} else if (!cartRepository.existsByUserId(userId)) {
			return cartService.createCart(currentUser, resp);
			// 회원인데 카트가 있는 경우
		} else {
			return cartRepository.findByUserId(userId).orElseThrow(() -> new UserCartNotFoundException(userId));
		}
	}

}
