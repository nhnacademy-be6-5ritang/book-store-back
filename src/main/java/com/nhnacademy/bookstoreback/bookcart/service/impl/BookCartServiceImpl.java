package com.nhnacademy.bookstoreback.bookcart.service.impl;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nhnacademy.bookstoreback.auth.jwt.dto.CurrentUserDetails;
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
import com.nhnacademy.bookstoreback.cart.exception.UserCartNotFoundException;
import com.nhnacademy.bookstoreback.cart.repository.CartRepository;
import com.nhnacademy.bookstoreback.cart.service.CartService;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class BookCartServiceImpl implements BookCartService {
	private final BookRepository bookRepository;
	private final CartRepository cartRepository;
	private final CartService cartService;
	private final HttpServletResponse resp;
	private final BookCartRepository bookCartRepository;
	private final RedisTemplate<String, Object> cartRedisTemplate;

	@Override
	public List<GetBookCartResponse> getBookCartsByCartId(CurrentUserDetails currentUser, String cartId) {
		String nowCartId = setupCart(currentUser, cartId);

		return bookCartRepository.findById(nowCartId)
			.orElseThrow(() -> new BookCartNotFoundException(nowCartId))
			.getBookBundles()
			.stream()
			.map(book -> GetBookCartResponse.fromEntity(bookRepository.findById(book.getBookId())
				.orElseThrow(() -> new BookNotFoundException(book.getBookId())), book.getBookQuantity(), nowCartId))
			.toList();
	}

	@Override
	public void createBookCart(CurrentUserDetails currentUser, CreateBookCartRequest request, String cartId) {
		String nowCartId = setupCart(currentUser, cartId);

		bookRepository.findById(request.bookId())
			.orElseThrow(() -> new BookNotFoundException(request.bookId()));

		BookCart bookCart = bookCartRepository.findById(nowCartId)
			.orElseThrow(() -> new BookCartNotFoundException(nowCartId));

		if (bookCart.getBookBundles().stream()
			.anyMatch(book -> book.getBookId().equals(request.bookId()))) {
			throw new BookCartAlreadyExistsException(request.bookId());
		}

		saveWithTtl(BookCart.toEntity(bookCart, request));
	}

	@Override
	public void updateBookCart(Long bookId, CurrentUserDetails currentUser, UpdateBookCartRequest request,
		String cartId) {
		String nowCartId = setupCart(currentUser, cartId);

		BookCart bookCart = bookCartRepository.findById(nowCartId)
			.orElseThrow(() -> new BookCartNotFoundException(nowCartId));

		bookCart.updateBookQuantity(bookId, request.bookQuantity());

		saveWithTtl(bookCart);
	}

	@Override
	public void deleteBookCart(Long bookId, CurrentUserDetails currentUser, String cartId) {
		String nowCartId = setupCart(currentUser, cartId);

		BookCart bookCart = bookCartRepository.findById(nowCartId)
			.orElseThrow(() -> new BookCartNotFoundException(nowCartId));

		bookCart.removeBook(bookId);
		saveWithTtl(bookCart);
	}

	public String setupCart(CurrentUserDetails currentUser, String cartId) {
		Long userId = currentUser != null ? currentUser.getUserId() : null;

		// 비회원인데 카트가 없는 경우
		if (userId == null && cartId.isEmpty()) {
			return cartService.createCart(currentUser, resp).getCartId();
			// 비회원인데 카트가 있는 경우
		} else if (userId == null) {
			return cartId;
			// 회원인데 카트가 없는 경우
		} else if (!cartRepository.existsByUserId(userId)) {
			return cartService.createCart(currentUser, resp).getCartId();
			// 회원인데 카트가 있는 경우
		} else {
			return cartRepository.findByUserId(userId)
				.orElseThrow(() -> new UserCartNotFoundException(userId))
				.getCartId();
		}
	}

	// 유효기간 유지
	public <S extends BookCart> void saveWithTtl(S entity) {
		String key = "bookCarts:" + entity.getCartId();
		Long remainingTtl = cartRedisTemplate.getExpire(key, TimeUnit.SECONDS);

		bookCartRepository.save(entity);

		if (remainingTtl != null && remainingTtl > 0) {
			cartRedisTemplate.expire(key, remainingTtl, TimeUnit.SECONDS);
		}

	}

}
