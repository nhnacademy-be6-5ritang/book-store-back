package com.nhnacademy.bookstoreback.wishlist.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.nhnacademy.bookstoreback.book.domain.entity.Book;
import com.nhnacademy.bookstoreback.book.repository.BookRepository;
import com.nhnacademy.bookstoreback.global.exception.NotFoundException;
import com.nhnacademy.bookstoreback.global.exception.payload.ErrorStatus;
import com.nhnacademy.bookstoreback.user.domain.entity.User;
import com.nhnacademy.bookstoreback.user.repository.UserRepository;
import com.nhnacademy.bookstoreback.wishlist.domain.dto.request.CreateWishListRequest;
import com.nhnacademy.bookstoreback.wishlist.domain.dto.response.CreateWishListResponse;
import com.nhnacademy.bookstoreback.wishlist.domain.dto.response.GetWishListResponse;
import com.nhnacademy.bookstoreback.wishlist.domain.entity.WishList;
import com.nhnacademy.bookstoreback.wishlist.repository.WishListRepository;
import com.nhnacademy.bookstoreback.wishlist.service.WishListService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WishListServiceImpl implements WishListService {
	private final WishListRepository wishListRepository;
	private final BookRepository bookRepository;
	private final UserRepository userRepository;

	@Override
	public List<GetWishListResponse> getWishLists(Long userId) {
		List<WishList> wishLists = wishListRepository.findAllByUserId(userId);
		return wishLists.stream()
			.map(wishList -> GetWishListResponse.builder()
				.bookId(wishList.getBook().getBookId())
				.build())
			.toList();
	}

	@Override
	public CreateWishListResponse createWishList(CreateWishListRequest request) {
		Book book = bookRepository.findById(request.bookId()).orElseThrow(() -> {
			String errorMessage = String.format("해당 도서 '%s'는 존재하지 않는 도서입니다.", request.bookId());
			ErrorStatus errorStatus = ErrorStatus.from(errorMessage, HttpStatus.NOT_FOUND, LocalDateTime.now());
			return new NotFoundException(errorStatus);
		});

		User user = userRepository.findById(request.userId()).orElseThrow(() -> {
			String errorMessage = String.format("해당 회원 '%s'는 존재하지 않는 회원입니다.", request.userId());
			ErrorStatus errorStatus = ErrorStatus.from(errorMessage, HttpStatus.NOT_FOUND, LocalDateTime.now());
			return new NotFoundException(errorStatus);
		});

		WishList wishList = new WishList(book, user);
		wishListRepository.save(wishList);

		return CreateWishListResponse.builder()
			.bookId(book.getBookId())
			.userId(user.getId())
			.build();
	}

	@Override
	public void deleteWishList(Long wishListId) {
		wishListRepository.deleteById(wishListId);
	}
}
