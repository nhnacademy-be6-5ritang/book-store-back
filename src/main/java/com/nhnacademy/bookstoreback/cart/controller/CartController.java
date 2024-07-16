package com.nhnacademy.bookstoreback.cart.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nhnacademy.bookstoreback.auth.annotation.CurrentUser;
import com.nhnacademy.bookstoreback.auth.jwt.dto.CurrentUserDetails;
import com.nhnacademy.bookstoreback.cart.domain.dto.response.GetCartResponse;
import com.nhnacademy.bookstoreback.cart.service.CartService;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

/**
 * @author 이경헌
 * 장바구니 관련 기능을 제공하는 컨트롤러입니다.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/carts")
public class CartController {
	private final CartService cartService;

	/**
	 * 특정 장바구니 정보를 조회합니다.
	 *
	 * @param cartId 장바구니 ID
	 * @return 조회된 장바구니 정보 (GetCartResponse 객체)
	 */
	@GetMapping("/{cartId}")
	public ResponseEntity<GetCartResponse> getCart(@PathVariable String cartId) {
		return ResponseEntity.ok(cartService.getCart(cartId));
	}

	/**
	 * 현재 사용자의 장바구니를 생성합니다.
	 *
	 * @param currentUser 현재 사용자의 정보 (CurrentUserDetails 객체)
	 * @param resp        HttpServletResponse 객체
	 * @return HTTP 상태 코드 201(CREATED)
	 */
	@PostMapping
	public ResponseEntity<Void> createCart(@CurrentUser CurrentUserDetails currentUser,
		HttpServletResponse resp) {
		cartService.createCart(currentUser, resp);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
}
