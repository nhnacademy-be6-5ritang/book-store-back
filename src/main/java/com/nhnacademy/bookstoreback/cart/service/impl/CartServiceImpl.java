package com.nhnacademy.bookstoreback.cart.service.impl;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nhnacademy.bookstoreback.cart.domain.dto.response.CreateCartResponse;
import com.nhnacademy.bookstoreback.cart.domain.dto.response.GetCartResponse;
import com.nhnacademy.bookstoreback.cart.domain.entity.Cart;
import com.nhnacademy.bookstoreback.cart.repository.CartRepository;
import com.nhnacademy.bookstoreback.cart.service.CartService;
import com.nhnacademy.bookstoreback.global.exception.AlreadyExistsException;
import com.nhnacademy.bookstoreback.global.exception.NotFoundException;
import com.nhnacademy.bookstoreback.global.exception.UserNotFoundException;
import com.nhnacademy.bookstoreback.global.exception.payload.ErrorStatus;
import com.nhnacademy.bookstoreback.user.domain.entity.User;
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

	@Override
	public CreateCartResponse createCart(HttpServletRequest req, HttpServletResponse resp, Long userId) {
		Cart cart = null;
		if (userId == null) {
			// userId가 null일 경우 쿠키에서 cartId를 가져옴
			Long cartId = null;
			Cookie[] cookies = req.getCookies();
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

				Cookie newCookie = new Cookie("cartId", String.valueOf(cartId));
				newCookie.setPath("/");
				newCookie.setHttpOnly(true);
				newCookie.setMaxAge(7 * 24 * 60 * 60); // 7일간 유효
				resp.addCookie(newCookie);
			} else {
				String errorMessage = String.format("해당 카트 '%d'은 이미 존재하는 카트입니다.", cartId);
				ErrorStatus errorStatus = ErrorStatus.from(errorMessage, HttpStatus.CONFLICT, LocalDateTime.now());
				throw new AlreadyExistsException(errorStatus);
			}

		} else if (!cartRepository.existsByUserId(userId)) {
			// userId가 null이 아닌 경우 userId로 Cart 생성
			User user = userRepository.findById(userId).orElseThrow(() -> {
				String errorMessage = String.format("해당 사용자 '%s'는 존재하지 않는 사용자입니다.", userId);
				ErrorStatus errorStatus = ErrorStatus.from(errorMessage, HttpStatus.NOT_FOUND, LocalDateTime.now());
				return new UserNotFoundException(errorStatus);
			});

			cart = cartRepository.save(new Cart(user));
		} else {
			// userId가 null이 아닌데 카트를 가지고 있는 경우 예외처리
			String errorMessage = String.format("해당 유저 '%d'은 이미 카트를 가지고 있습니다.", userId);
			ErrorStatus errorStatus = ErrorStatus.from(errorMessage, HttpStatus.CONFLICT, LocalDateTime.now());
			throw new AlreadyExistsException(errorStatus);
		}

		return CreateCartResponse.builder()
			.cardId(cart.getCartId())
			.userId(userId)
			.build();
	}

	@Transactional(readOnly = true)
	@Override
	public GetCartResponse getCart(Long cardId) {
		Cart cart = cartRepository.findById(cardId).orElseThrow(() -> {
			String errorMessage = String.format("해당 카트 '%d'은 존재하지 않는 카트입니다.", cardId);
			ErrorStatus errorStatus = ErrorStatus.from(errorMessage, HttpStatus.NOT_FOUND, LocalDateTime.now());
			return new NotFoundException(errorStatus);
		});

		return GetCartResponse.builder()
			.cartId(cart.getCartId())
			.userId(Optional.ofNullable(cart.getUser()).map(User::getId).orElse(null))
			.build();
	}
}
