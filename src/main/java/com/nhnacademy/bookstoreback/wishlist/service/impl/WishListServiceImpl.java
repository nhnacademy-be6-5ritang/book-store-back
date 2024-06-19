package com.nhnacademy.bookstoreback.wishlist.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.nhnacademy.bookstoreback.wishlist.domain.dto.response.GetWishListResponse;
import com.nhnacademy.bookstoreback.wishlist.domain.entity.WishList;
import com.nhnacademy.bookstoreback.wishlist.repository.WishListRepository;
import com.nhnacademy.bookstoreback.wishlist.service.WishListService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WishListServiceImpl implements WishListService {
	private final WishListRepository wishListRepository;

	@Override
	public List<GetWishListResponse> findAllByUserId(Long userId) {
		List<WishList> wishLists = wishListRepository.findAllByUserId(userId);
		// return wishLists.stream()
		// 	.map(wishList -> new GetWishListResponse(wishList.getBookId()))
		// 	.collect(Collectors.toList());
		return null;
	}

	@Override
	public WishList createWishList(WishList wishList) {
		return wishListRepository.save(wishList);
	}

	@Override
	public void deleteWishList(Long wishListId) {
		wishListRepository.deleteById(wishListId);
	}
}
