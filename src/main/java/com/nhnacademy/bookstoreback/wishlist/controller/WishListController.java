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

import com.nhnacademy.bookstoreback.auth.annotation.CurrentUser;
import com.nhnacademy.bookstoreback.auth.jwt.dto.CurrentUserDetails;
import com.nhnacademy.bookstoreback.wishlist.domain.dto.request.CreateWishListRequest;
import com.nhnacademy.bookstoreback.wishlist.domain.dto.response.GetWishListResponse;
import com.nhnacademy.bookstoreback.wishlist.service.WishListService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * @author 이경헌
 * 위시리스트와 관련된 HTTP 요청을 처리하는 컨트롤러입니다.
 * 사용자는 위시리스트를 조회하고, 생성하고, 삭제할 수 있습니다.
 */
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
	@DeleteMapping("/{wishListId}")
	public ResponseEntity<Void> deleteWishList(@PathVariable Long wishListId,
		@CurrentUser CurrentUserDetails currentUser) {
		wishListService.deleteWishList(wishListId, currentUser);
		return ResponseEntity.ok().build();
	}
}
