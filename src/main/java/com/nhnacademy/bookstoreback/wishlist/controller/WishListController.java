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

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/wishLists")
public class WishListController {
	private final WishListService wishListService;

	@GetMapping
	public ResponseEntity<List<GetWishListResponse>> getWishLists(@CurrentUser CurrentUserDetails currentUser) {
		return ResponseEntity.status(HttpStatus.OK).body(wishListService.getWishLists(currentUser));
	}

	@PostMapping
	public ResponseEntity<Void> createWishList(@CurrentUser CurrentUserDetails currentUser,
		@RequestBody CreateWishListRequest request) {
		wishListService.createWishList(currentUser, request);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@DeleteMapping("/{wishListId}")
	public ResponseEntity<Void> deleteWishList(@PathVariable Long wishListId,
		@CurrentUser CurrentUserDetails currentUser) {
		wishListService.deleteWishList(wishListId, currentUser);
		return ResponseEntity.noContent().build();
	}
}
