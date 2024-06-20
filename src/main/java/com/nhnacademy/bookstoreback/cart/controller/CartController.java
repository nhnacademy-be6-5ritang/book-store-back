package com.nhnacademy.bookstoreback.cart.controller;

import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nhnacademy.bookstoreback.cart.domain.dto.response.CreateCartResponse;
import com.nhnacademy.bookstoreback.cart.domain.entity.Cart;
import com.nhnacademy.bookstoreback.cart.service.CartService;
import com.nhnacademy.bookstoreback.user.service.UserService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/carts")
public class CartController {
	private final CartService cartService;
	private final UserService userService;

	@PostMapping()
	public ResponseEntity<CreateCartResponse> createCart(@RequestParam(required = false) Long userId,
		HttpServletRequest request,
		HttpServletResponse response) {
		Cart cart = null;

		if (userId == null) {
			// userId가 null일 경우 쿠키에서 cartId를 가져옴
			Long cartId = null;
			Cookie[] cookies = request.getCookies();
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
				cart = cartService.createCart(new Cart(null));
				cartId = cart.getCartId();

				Cookie newCookie = new Cookie("cartId", String.valueOf(cartId));
				newCookie.setPath("/");
				newCookie.setHttpOnly(true);
				newCookie.setMaxAge(7 * 24 * 60 * 60); // 7일간 유효
				response.addCookie(newCookie);
			}

		} else {
			// userId가 null이 아닌 경우 userId로 Cart 생성
			// cart = cartService.createCart(new Cart(userService.findByUserId(userId)));
		}

		CreateCartResponse createCartResponse = new CreateCartResponse(
			Objects.requireNonNull(cart).getCartId(), cart.getUser().getId());

		return ResponseEntity.status(HttpStatus.CREATED).body(createCartResponse);
	}

	@GetMapping("/{cartId}")
	public ResponseEntity<Cart> getCart(@PathVariable Long cartId) {
		Cart cart = cartService.getCart(cartId);
		return ResponseEntity.ok(cart);
	}
}
