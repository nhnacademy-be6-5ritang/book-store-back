package com.nhnacademy.bookstoreback.cart.service.impl;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nhnacademy.bookstoreback.cart.domain.dto.response.GetCartResponse;
import com.nhnacademy.bookstoreback.cart.domain.entity.Cart;
import com.nhnacademy.bookstoreback.cart.repository.CartRepository;
import com.nhnacademy.bookstoreback.cart.service.CartService;
import com.nhnacademy.bookstoreback.global.exception.CartNotFoundException;
import com.nhnacademy.bookstoreback.global.exception.payload.ErrorStatus;
import com.nhnacademy.bookstoreback.user.domain.entity.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CartServiceImpl implements CartService {
	private final CartRepository cartRepository;

	@Override
	public Cart createCart(Cart cart) {
		return cartRepository.save(cart);
	}

	@Transactional(readOnly = true)
	@Override
	public GetCartResponse getCart(Long cardId) {
		Cart cart = cartRepository.findById(cardId).orElseThrow(() -> {
			String errorMessage = String.format("해당 카트 '%d'은 존재하지 않는 카트입니다.", cardId);
			ErrorStatus errorStatus = ErrorStatus.from(errorMessage, HttpStatus.NOT_FOUND, LocalDateTime.now());
			return new CartNotFoundException(errorStatus);
		});

		return GetCartResponse.builder()
			.cartId(cart.getCartId())
			.userId(Optional.ofNullable(cart.getUser()).map(User::getId).orElse(null))
			.build();
	}
}
