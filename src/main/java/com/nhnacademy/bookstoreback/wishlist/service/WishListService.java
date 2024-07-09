package com.nhnacademy.bookstoreback.wishlist.service;

import java.util.List;

import com.nhnacademy.bookstoreback.auth.jwt.dto.CurrentUserDetails;
import com.nhnacademy.bookstoreback.wishlist.domain.dto.request.CreateWishListRequest;
import com.nhnacademy.bookstoreback.wishlist.domain.dto.response.GetWishListResponse;

public interface WishListService {

	/**
	 * 특정 사용자의 위시리스트 목록을 조회합니다.
	 *
	 * @param currentUser 현재 사용자의 인증 정보를 담고 있는 CurrentUserDetails 객체
	 * @return 위시리스트 목록 (GetWishListResponse 객체의 리스트)
	 */
	List<GetWishListResponse> getWishLists(CurrentUserDetails currentUser);

	/**
	 * 새로운 위시리스트를 생성합니다.
	 *
	 * @param currentUser 현재 사용자의 인증 정보를 담고 있는 CurrentUserDetails 객체
	 * @param request 생성할 위시리스트의 정보를 담은 CreateWishListRequest 객체
	 */
	void createWishList(CurrentUserDetails currentUser, CreateWishListRequest request);

	/**
	 * 특정 위시리스트를 삭제합니다.
	 *
	 * @param wishListId 삭제할 위시리스트의 ID
	 * @param currentUser 현재 사용자의 인증 정보를 담고 있는 CurrentUserDetails 객체
	 */
	void deleteWishList(Long wishListId, CurrentUserDetails currentUser);
}
