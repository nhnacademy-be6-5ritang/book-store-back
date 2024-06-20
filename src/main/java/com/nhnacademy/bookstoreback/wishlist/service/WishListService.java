package com.nhnacademy.bookstoreback.wishlist.service;

import java.util.List;

import com.nhnacademy.bookstoreback.wishlist.domain.dto.request.CreateWishListRequest;
import com.nhnacademy.bookstoreback.wishlist.domain.dto.response.CreateWishListResponse;
import com.nhnacademy.bookstoreback.wishlist.domain.dto.response.GetWishListResponse;

public interface WishListService {
	List<GetWishListResponse> getWishLists(Long userId);

	CreateWishListResponse createWishList(CreateWishListRequest request);

	void deleteWishList(Long wishListId);
}
