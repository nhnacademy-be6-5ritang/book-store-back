package com.nhnacademy.bookstoreback.wishlist.service;

import java.util.List;

import com.nhnacademy.bookstoreback.wishlist.domain.dto.response.GetWishListResponse;
import com.nhnacademy.bookstoreback.wishlist.domain.entity.WishList;

public interface WishListService {
	List<GetWishListResponse> findAllByUserId(Long userId);

	WishList createWishList(WishList wishList);

	void deleteWishList(Long wishListId);
}
