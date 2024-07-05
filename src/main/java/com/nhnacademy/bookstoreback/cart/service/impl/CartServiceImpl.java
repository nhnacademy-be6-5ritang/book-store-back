package com.nhnacademy.bookstoreback.cart.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nhnacademy.bookstoreback.auth.annotation.CurrentUser;
import com.nhnacademy.bookstoreback.auth.jwt.dto.CurrentUserDetails;
import com.nhnacademy.bookstoreback.cart.domain.dto.request.CreateCartRequest;
import com.nhnacademy.bookstoreback.cart.domain.dto.request.GetCartRequest;
import com.nhnacademy.bookstoreback.cart.domain.dto.response.CreateCartResponse;
import com.nhnacademy.bookstoreback.cart.domain.dto.response.GetCartResponse;
import com.nhnacademy.bookstoreback.cart.domain.entity.Cart;
import com.nhnacademy.bookstoreback.cart.exception.CartAlreadyExistsException;
import com.nhnacademy.bookstoreback.cart.exception.UserCartAlreadyExistsException;
import com.nhnacademy.bookstoreback.cart.repository.CartRepository;
import com.nhnacademy.bookstoreback.cart.service.CartService;
import com.nhnacademy.bookstoreback.user.domain.entity.User;
import com.nhnacademy.bookstoreback.user.exception.UserNotFoundException;
import com.nhnacademy.bookstoreback.user.repository.UserRepository;

import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CartServiceImpl implements CartService {
	private final CartRepository cartRepository;
	private final UserRepository userRepository;

	@Override
	public CreateCartResponse createCart(Long userId, CreateCartRequest request) {
		Cart cart = null;
		Cookie newCookie = null;
		if (userId == null) {
			// userId가 null일 경우 쿠키에서 cartId를 가져옴
			Long cartId = null;
			Cookie[] cookies = request.cookies();
			if (cookies != null) {
				for (Cookie cookie : cookies) {
					if ("cartId".equals(cookie.getName())) {
						cartId = Long.valueOf(cookie.getValue());
						break;
					}
				}
			}

			if (cartId == null) {
				// 쿠키에 cartId가 없으면 새로 생성하여 쿠키에 설정
				cart = cartRepository.save(new Cart(null));
				cartId = cart.getCartId();

				newCookie = new Cookie("cartId", String.valueOf(cartId));
				newCookie.setPath("/");
				newCookie.setHttpOnly(true);
				newCookie.setMaxAge(7 * 24 * 60 * 60); // 7일간 유효
				// resp.addCookie(newCookie);
			} else {
				throw new CartAlreadyExistsException(cartId);
			}
		} else if (!cartRepository.existsByUserId(userId)) {
			// userId가 null 이 아닌대 카트가 없는 경우 userId로 Cart 생성
			User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
			cart = cartRepository.save(new Cart(user));
		} else {
			// userId가 null 이 아닌데 카트를 가지고 있는 경우 예외처리
			throw new UserCartAlreadyExistsException(userId);
		}

		return CreateCartResponse.fromEntity(newCookie, cart);
	}

	@Transactional(readOnly = true)
	@Override
	public GetCartResponse getCart(Long userId, GetCartRequest request) {

		Cart cart = null;
		Cookie newCookie = null;
		if (userId == null) {
			// userId가 null일 경우 쿠키에서 cartId를 가져옴
			Long cartId = null;
			Cookie[] cookies = request.cookies();
			if (cookies != null) {
				for (Cookie cookie : cookies) {
					if ("cartId".equals(cookie.getName())) {
						cartId = Long.valueOf(cookie.getValue());
						break;
					}
				}
			}

			if (cartId == null) {
				// 쿠키에 cartId가 없으면 새로 생성하여 쿠키에 설정
				cart = cartRepository.save(new Cart(null));
				cartId = cart.getCartId();

				newCookie = new Cookie("cartId", String.valueOf(cartId));
				newCookie.setPath("/");
				newCookie.setHttpOnly(true);
				newCookie.setMaxAge(7 * 24 * 60 * 60); // 7일간 유효
				// resp.addCookie(newCookie);
			} else {
				throw new CartAlreadyExistsException(cartId);
			}
		} else if (!cartRepository.existsByUserId(userId)) {
			// userId가 null 이 아닌대 카트가 없는 경우 userId로 Cart 생성
			User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
			cart = cartRepository.save(new Cart(user));
		} else {
			// userId가 null 이 아닌데 카트를 가지고 있는 경우 예외처리
			throw new UserCartAlreadyExistsException(userId);
		}

		return GetCartResponse.fromEntity(newCookie, cart);
	}

	public boolean memberCheck(@CurrentUser CurrentUserDetails currentUserDetails) {
		Long userId = currentUserDetails.getUserId();

		return userId != null;

	}
}
