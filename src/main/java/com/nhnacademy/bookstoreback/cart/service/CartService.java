package com.nhnacademy.bookstoreback.cart.service;

import com.nhnacademy.bookstoreback.auth.jwt.dto.CurrentUserDetails;
import com.nhnacademy.bookstoreback.cart.domain.dto.response.GetCartResponse;
import com.nhnacademy.bookstoreback.cart.domain.entity.Cart;

import jakarta.servlet.http.HttpServletResponse;

/**
 * @author 이경헌
 * CartService는 장바구니와 관련된 비즈니스 로직을 처리하는 인터페이스입니다.
 */

public interface CartService {
	/**
	 * 특정 장바구니의 정보를 조회합니다.
	 *
	 * @param cartId 조회할 장바구니의 ID
	 * @return 장바구니 정보가 포함된 ResponseEntity 객체
	 */
	GetCartResponse getCart(String cartId);

	/**
	 * 새로운 장바구니를 생성합니다.
	 *
	 * @return 생성된 장바구니의 정보가 포함된 ResponseEntity 객체
	 */
	Cart createCart(CurrentUserDetails currentUser, HttpServletResponse resp);

}
