package com.nhnacademy.bookstoreback.bookcart.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nhnacademy.bookstoreback.auth.annotation.CurrentUser;
import com.nhnacademy.bookstoreback.auth.jwt.dto.CurrentUserDetails;
import com.nhnacademy.bookstoreback.bookcart.domain.dto.request.CreateBookCartRequest;
import com.nhnacademy.bookstoreback.bookcart.domain.dto.request.UpdateBookCartRequest;
import com.nhnacademy.bookstoreback.bookcart.domain.dto.response.GetBookCartResponse;
import com.nhnacademy.bookstoreback.bookcart.service.BookCartService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * @author 이경헌
 * 현재 사용자의 도서 장바구니 관련 기능을 제공하는 컨트롤러입니다.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/carts/me")
public class BookCartController {
	private final BookCartService bookCartService;

	/**
	 * 현재 사용자의 장바구니에 있는 도서 목록을 조회합니다.
	 *
	 * @param currentUser 현재 사용자의 인증 및 정보
	 * @param cartId      쿠키에서 가져온 장바구니 ID (선택적)
	 * @return 도서 장바구니 목록 응답
	 */
	@GetMapping
	public ResponseEntity<List<GetBookCartResponse>> getBookCarts(
		@CurrentUser CurrentUserDetails currentUser, @CookieValue(name = "cartId", required = false) String cartId) {
		return ResponseEntity.status(HttpStatus.OK)
			.body(bookCartService.getBookCartsByCartId(currentUser, cartId));
	}

	/**
	 * 현재 사용자의 장바구니에 새로운 도서를 추가합니다.
	 *
	 * @param currentUser 현재 사용자의 인증 및 정보
	 * @param request     추가할 도서의 정보 요청
	 * @param cartId      쿠키에서 가져온 장바구니 ID (선택적)
	 * @return HTTP 상태 코드 201(CREATED)
	 */
	@PostMapping
	public ResponseEntity<Void> createBookCart(@CurrentUser CurrentUserDetails currentUser,
		@Valid @RequestBody CreateBookCartRequest request,
		@CookieValue(name = "cartId", required = false) String cartId) {
		bookCartService.createBookCart(currentUser, request, cartId);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	/**
	 * 현재 사용자의 장바구니에 있는 특정 도서 정보를 수정합니다.
	 *
	 * @param currentUser 현재 사용자의 인증 및 정보
	 * @param bookId      수정할 도서의 ID
	 * @param request     수정할 도서 정보 요청
	 * @param cartId      쿠키에서 가져온 장바구니 ID (선택적)
	 * @return HTTP 상태 코드 200(OK)
	 */
	@PutMapping("/{bookId}")
	public ResponseEntity<Void> updateBookCart(@CurrentUser CurrentUserDetails currentUser,
		@PathVariable Long bookId, @Valid @RequestBody UpdateBookCartRequest request,
		@CookieValue(name = "cartId", required = false) String cartId) {
		bookCartService.updateBookCart(bookId, currentUser, request, cartId);
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	/**
	 * 현재 사용자의 장바구니에서 특정 도서를 삭제합니다.
	 *
	 * @param currentUser 현재 사용자의 인증 및 정보
	 * @param bookId      삭제할 도서의 ID
	 * @param cartId      쿠키에서 가져온 장바구니 ID (선택적)
	 * @return HTTP 상태 코드 200(OK)
	 */
	@DeleteMapping("/{bookId}")
	public ResponseEntity<Void> deleteBookCart(@CurrentUser CurrentUserDetails currentUser,
		@PathVariable Long bookId, @CookieValue(name = "cartId", required = false) String cartId) {
		bookCartService.deleteBookCart(bookId, currentUser, cartId);
		return ResponseEntity.ok().build();
	}
}
