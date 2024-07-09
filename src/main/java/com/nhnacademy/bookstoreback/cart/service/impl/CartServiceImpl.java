package com.nhnacademy.bookstoreback.cart.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nhnacademy.bookstoreback.auth.jwt.dto.CurrentUserDetails;
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
	public GetCartResponse getCart(Long cartId) {
		Cart cart = cartRepository.findById(cartId).orElseThrow(() -> new CartNotFoundException(cartId));
		return GetCartResponse.fromEntity(cart);
	}

	@Override
	public Cart createCart(CurrentUserDetails currentUser, HttpServletResponse resp) {
		Long userId = currentUser != null ? currentUser.getUserId() : null;
		Cart cart = null;
		// 비회원인 경우
		if (userId == null) {
			cart = cartRepository.save(new Cart(null));
			CookieUtil.addCookie(resp, "cartId", cart.getCartId(), 7 * 24 * 60 * 60);
		} else {
			// 회원인 경우
			User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
			if (cartRepository.existsByUserId(userId)) {
				throw new CartAlreadyExistsException(userId);
			}
			cart = cartRepository.save(new Cart(user));
		}
		return cart;
	}
}
