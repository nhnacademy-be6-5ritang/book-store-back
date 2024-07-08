package com.nhnacademy.bookstoreback.wishlist.domain.dto.response;

import java.math.BigDecimal;

import com.nhnacademy.bookstoreback.wishlist.domain.entity.WishList;

import lombok.Builder;

@Builder
public record GetWishListResponse(
	Long wishListId,
	String bookImageUrl,
	String bookTitle,
	String authorName,
	String publisherName,
	BigDecimal bookSalePrice,
	BigDecimal bookSalePercent
) {
	public static GetWishListResponse fromEntity(WishList wishList) {
		String imageUrl = wishList.getBook().getBookImages().stream()
			.map(bookImage -> bookImage.getImage().getImageUrl())
			.findFirst()
			.orElse(null); // 이미지가 없는 경우 null 반환

		return GetWishListResponse.builder()
			.wishListId(wishList.getWishListId())
			.bookImageUrl(imageUrl)
			.bookTitle(wishList.getBook().getBookTitle())
			.authorName(wishList.getBook().getAuthor().getAuthorName())
			.publisherName(wishList.getBook().getPublisher().getPublisherName())
			.bookSalePrice(wishList.getBook().getBookSalePrice())
			.bookSalePercent(wishList.getBook().getBookSalePercent())
			.build();
	}
}
