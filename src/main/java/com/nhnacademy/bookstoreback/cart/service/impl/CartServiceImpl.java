package com.nhnacademy.bookstoreback.cart.service.impl;

import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nhnacademy.bookstoreback.cart.domain.dto.response.GetCartResponse;
import com.nhnacademy.bookstoreback.cart.domain.entity.Cart;
import com.nhnacademy.bookstoreback.cart.exception.CartAlreadyExistsException;
import com.nhnacademy.bookstoreback.cart.exception.CartNotFoundException;
import com.nhnacademy.bookstoreback.cart.repository.CartRepository;
import com.nhnacademy.bookstoreback.cart.service.CartService;
import com.nhnacademy.bookstoreback.global.util.CookieUtil;
import com.nhnacademy.bookstoreback.user.domain.entity.User;
import com.nhnacademy.bookstoreback.user.exception.UserNotFoundException;
import com.nhnacademy.bookstoreback.user.repository.UserRepository;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CartServiceImpl implements CartService {
	private final CartRepository cartRepository;
	private final UserRepository userRepository;

	@Transactional(readOnly = true)
	@Override
	public GetCartResponse getCart(Long userId, HttpServletRequest req) {
		Cart cart;

		// 비회원인 경우
		if (userId == null) {
			Cookie cookie = CookieUtil.getCookie(req, "cartId");
			cart = cartRepository.findById(Long.valueOf(Objects.requireNonNull(cookie).getValue()))
				.orElseThrow(() -> new CartNotFoundException(cookie.getValue()));
		} else {
			// 회원인 경우
			cart = cartRepository.findById(userId).orElseThrow(() -> new CartNotFoundException(userId));
		}

		return GetCartResponse.fromEntity(cart);
	}

	@Override
	public void createCart(Long userId, HttpServletResponse response) {
		// 비회원인 경우
		if (userId == null) {
			Cart cart = cartRepository.save(new Cart(null));
			CookieUtil.addCookie(response, "cartId", cart.getCartId(), 7 * 24 * 60 * 60);
		} else {
			// 회원인 경우
			User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
			if (cartRepository.existsByUserId(userId)) {
				throw new CartAlreadyExistsException(userId);
			}
			cartRepository.save(new Cart(user));
		}
	}

}
