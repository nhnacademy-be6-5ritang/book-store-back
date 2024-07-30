package com.nhnacademy.bookstoreback.wishlist.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nhnacademy.bookstoreback.auth.annotation.AuthorizeRole;
import com.nhnacademy.bookstoreback.auth.annotation.CurrentUser;
import com.nhnacademy.bookstoreback.auth.jwt.dto.CurrentUserDetails;
import com.nhnacademy.bookstoreback.wishlist.domain.dto.request.CreateWishListRequest;
import com.nhnacademy.bookstoreback.wishlist.domain.dto.response.GetWishListResponse;
import com.nhnacademy.bookstoreback.wishlist.service.WishListService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * @author 이경헌
 * 위시리스트와 관련된 HTTP 요청을 처리하는 컨트롤러입니다.
 */
@Tag(name = "WishList", description = "위시리스트 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/wishLists")
public class WishListController {
	private final WishListService wishListService;

	/**
	 * 현재 사용자에 대한 위시리스트를 조회합니다.
	 *
	 * @param currentUser 현재 사용자 정보
	 * @return 현재 사용자의 위시리스트 목록
	 */
	@Operation(
		summary = "현재 사용자 위시리스트 조회",
		description = "현재 사용자의 위시리스트를 조회합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "위시리스트 조회 성공")
	})
	@AuthorizeRole({"MEMBER", "HEAD_ADMIN"})
	@GetMapping
	public ResponseEntity<List<GetWishListResponse>> getWishLists(@CurrentUser CurrentUserDetails currentUser) {
		return ResponseEntity.status(HttpStatus.OK).body(wishListService.getWishLists(currentUser));
	}

	/**
	 * 현재 사용자의 위시리스트에 책을 추가합니다.
	 *
	 * @param currentUser 현재 사용자 정보
	 * @param request     위시리스트에 추가할 책 정보
	 * @return HTTP 상태 코드 201(Created)
	 */
	@Operation(
		summary = "위시리스트에 책 추가",
		description = "현재 사용자의 위시리스트에 책을 추가합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "201", description = "책이 위시리스트에 성공적으로 추가됨"),
		@ApiResponse(responseCode = "400", description = "잘못된 요청 데이터입니다."),
		@ApiResponse(responseCode = "404", description = "요청한 리소스를 찾을 수 없음")
	})
	@AuthorizeRole({"MEMBER", "HEAD_ADMIN"})
	@PostMapping
	public ResponseEntity<Void> createWishList(@CurrentUser CurrentUserDetails currentUser,
		@Valid @RequestBody CreateWishListRequest request) {
		wishListService.createWishList(currentUser, request);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	/**
	 * 현재 사용자의 위시리스트에서 특정 책을 삭제합니다.
	 *
	 * @param wishListId  삭제할 위시리스트 ID
	 * @param currentUser 현재 사용자 정보
	 * @return HTTP 상태 코드 200(OK)
	 */
	@Operation(
		summary = "위시리스트에서 책 삭제",
		description = "현재 사용자의 위시리스트에서 특정 책을 삭제합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "위시리스트에서 책이 성공적으로 삭제됨"),
		@ApiResponse(responseCode = "404", description = "위시리스트 또는 유저를 찾을 수 없음")
	})
	@AuthorizeRole({"MEMBER", "HEAD_ADMIN"})
	@DeleteMapping("/{wishListId}")
	public ResponseEntity<Void> deleteWishList(@PathVariable Long wishListId,
		@CurrentUser CurrentUserDetails currentUser) {
		wishListService.deleteWishList(wishListId, currentUser);
		return ResponseEntity.ok().build();
	}
}
