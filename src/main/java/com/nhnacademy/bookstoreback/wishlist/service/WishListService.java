package com.nhnacademy.bookstoreback.wishlist.service;

import java.util.List;

import com.nhnacademy.bookstoreback.wishlist.domain.dto.request.CreateWishListRequest;
import com.nhnacademy.bookstoreback.wishlist.domain.dto.response.CreateWishListResponse;
import com.nhnacademy.bookstoreback.wishlist.domain.dto.response.GetWishListResponse;

public interface WishListService {
	/**
	 * 특정 사용자의 위시리스트 목록을 조회합니다.
	 *
	 * @param userId 사용자의 ID
	 * @return 위시리스트 목록 (GetWishListResponse 객체의 리스트)
	 */
	List<GetWishListResponse> getWishLists(Long userId);

	/**
	 * 새로운 위시리스트를 생성합니다.
	 *
	 * @param request 생성할 위시리스트의 정보를 담은 CreateWishListRequest 객체
	 * @return 생성된 위시리스트의 정보를 담은 CreateWishListResponse 객체
	 */
	CreateWishListResponse createWishList(CreateWishListRequest request);

	/**
	 * 특정 위시리스트를 삭제합니다.
	 *
	 * @param wishListId 삭제할 위시리스트의 ID
	 */
	void deleteWishList(Long wishListId);
}
