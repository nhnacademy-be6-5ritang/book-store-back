package com.nhnacademy.bookstoreback.bookcart.service.impl;

import java.util.List;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nhnacademy.bookstoreback.auth.jwt.dto.CurrentUserDetails;
import com.nhnacademy.bookstoreback.book.exception.BookNotFoundException;
import com.nhnacademy.bookstoreback.book.repository.BookRepository;
import com.nhnacademy.bookstoreback.bookcart.domain.dto.request.CreateBookCartRequest;
import com.nhnacademy.bookstoreback.bookcart.domain.dto.request.UpdateBookCartRequest;
import com.nhnacademy.bookstoreback.bookcart.domain.dto.response.GetBookCartResponse;
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
	private final RedisTemplate<String, Object> cartRedisTemplate;
	private final BookRepository bookRepository;
	private final CartRepository cartRepository;
	private final CartService cartService;
	private final HttpServletResponse resp;

	@Override
	public List<GetBookCartResponse> getBookCartsByCartId(CurrentUserDetails currentUser, String cartId) {
		cartId = setupCart(currentUser, cartId);

		return null;
	}

	@Override
	public void createBookCart(CurrentUserDetails currentUser, CreateBookCartRequest request, String cartId) {
		cartId = setupCart(currentUser, cartId);

		bookRepository.findById(request.bookId())
			.orElseThrow(() -> new BookNotFoundException(request.bookId()));

		// cartRedisTemplate.delete(redisKey);
		// cartRedisTemplate.opsForHash().put(cartId, "bookId", String.valueOf(request.bookId()));
		// cartRedisTemplate.opsForHash().put(cartId, "bookQuantity", String.valueOf(request.bookQuantity()));

		// bookCartRepository.save(new BookCart(cartId, request.bookId(), request.bookQuantity()));

		// // 장바구니를 이용할 때마다 유효기간을 갱신
		// if (currentUser == null) {
		// 	cartRedisTemplate.expire(redisKey, Duration.ofDays(7));
		// }

	}

	@Override
	public void updateBookCart(Long bookId, CurrentUserDetails currentUser, UpdateBookCartRequest request,
		String cartId) {
		setupCart(currentUser, cartId);

		// bookCart.updateBookQuantity(request.bookQuantity());
	}

	@Override
	public void deleteBookCart(Long bookId, CurrentUserDetails currentUser, String cartId) {
		cartId = setupCart(currentUser, cartId);

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

}
