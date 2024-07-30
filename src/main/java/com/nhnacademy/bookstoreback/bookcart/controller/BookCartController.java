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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * @author 이경헌
 * 현재 사용자의 도서 장바구니 관련 기능을 제공하는 컨트롤러입니다.
 */
@Tag(name = "BookCart", description = "장바구니 관련 API")
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
	@Operation(
		summary = "장바구니 조회",
		description = "현재 사용자의 장바구니에 있는 도서 목록을 조회합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "도서 장바구니 목록을 성공적으로 조회했습니다."),
	})
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
	@Operation(
		summary = "장바구니에 도서 추가",
		description = "현재 사용자의 장바구니에 새로운 도서를 추가합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "201", description = "도서가 장바구니에 성공적으로 추가되었습니다."),
		@ApiResponse(responseCode = "400", description = "잘못된 요청 데이터입니다."),
		@ApiResponse(responseCode = "404", description = "장바구니를 찾을 수 없습니다."),
		@ApiResponse(responseCode = "409", description = "도서가 이미 장바구니에 존재합니다.")
	})
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
	@Operation(
		summary = "장바구니 도서 수정",
		description = "현재 사용자의 장바구니에 있는 특정 도서의 정보를 수정합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "도서의 정보가 성공적으로 수정되었습니다."),
		@ApiResponse(responseCode = "400", description = "잘못된 요청 데이터입니다."),
		@ApiResponse(responseCode = "404", description = "장바구니를 찾을 수 없습니다.")
	})
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
	@Operation(
		summary = "장바구니 도서 삭제",
		description = "현재 사용자의 장바구니에서 특정 도서를 삭제합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "도서가 장바구니에서 성공적으로 삭제되었습니다."),
		@ApiResponse(responseCode = "404", description = "장바구니를 찾을 수 없습니다.")
	})
	@DeleteMapping("/{bookId}")
	public ResponseEntity<Void> deleteBookCart(@CurrentUser CurrentUserDetails currentUser,
		@PathVariable Long bookId, @CookieValue(name = "cartId", required = false) String cartId) {
		bookCartService.deleteBookCart(bookId, currentUser, cartId);
		return ResponseEntity.ok().build();
	}

	/**
	 * 현재 사용자의 장바구니에서 모든 도서를 삭제합니다.
	 *
	 * @param currentUser 현재 사용자의 인증 및 정보
	 * @param cartId      쿠키에서 가져온 장바구니 ID (선택적)
	 * @return HTTP 상태 코드 200(OK)
	 */
	@Operation(
		summary = "장바구니 비우기",
		description = "현재 사용자의 장바구니에서 모든 도서를 삭제합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "장바구니가 성공적으로 비워졌습니다."),
		@ApiResponse(responseCode = "404", description = "장바구니를 찾을 수 없습니다.")
	})
	@DeleteMapping("/all")
	public ResponseEntity<Void> deleteAllBookCart(@CurrentUser CurrentUserDetails currentUser,
		@CookieValue(name = "cartId", required = false) String cartId) {
		bookCartService.deleteAllBookCart(currentUser, cartId);
		return ResponseEntity.ok().build();
	}
}
