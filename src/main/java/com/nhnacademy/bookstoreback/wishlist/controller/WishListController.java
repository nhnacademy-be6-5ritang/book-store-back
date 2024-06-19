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

import com.nhnacademy.bookstoreback.wishlist.domain.dto.request.GetWishListsRequest;
import com.nhnacademy.bookstoreback.wishlist.domain.dto.response.GetWishListResponse;
import com.nhnacademy.bookstoreback.wishlist.domain.entity.WishList;
import com.nhnacademy.bookstoreback.wishlist.service.WishListService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/wishLists")
public class WishListController {
	private final WishListService wishListService;

	@PostMapping
	public ResponseEntity<WishList> createWishList(@RequestBody WishList wishList) {
		WishList wishList1 = wishListService.createWishList(wishList);
		return ResponseEntity.status(HttpStatus.CREATED).body(wishList1);
	}

	@GetMapping
	public ResponseEntity<List<GetWishListResponse>> getWishLists(@RequestBody GetWishListsRequest request) {
		List<GetWishListResponse> wishLists = wishListService.findAllByUserId(request.userId());
		return ResponseEntity.ok(wishLists);
	}

	@DeleteMapping("/{wishListId}")
	public ResponseEntity<Void> deleteWishList(@PathVariable Long wishListId) {
		wishListService.deleteWishList(wishListId);
		return ResponseEntity.noContent().build();
	}
}
