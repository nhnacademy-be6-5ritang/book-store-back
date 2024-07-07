package com.nhnacademy.bookstoreback.wishlist.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.nhnacademy.bookstoreback.book.domain.entity.Book;
import com.nhnacademy.bookstoreback.book.exception.BookNotFoundException;
import com.nhnacademy.bookstoreback.book.repository.BookRepository;
import com.nhnacademy.bookstoreback.user.domain.entity.User;
import com.nhnacademy.bookstoreback.user.exception.UserNotFoundException;
import com.nhnacademy.bookstoreback.user.repository.UserRepository;
import com.nhnacademy.bookstoreback.wishlist.domain.dto.request.CreateWishListRequest;
import com.nhnacademy.bookstoreback.wishlist.domain.dto.response.GetWishListResponse;
import com.nhnacademy.bookstoreback.wishlist.domain.entity.WishList;
import com.nhnacademy.bookstoreback.wishlist.exception.WishListAlreadyExistsException;
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
		return wishListRepository.findAllByUserId(userId).stream()
			.map(GetWishListResponse::fromEntity)
			.toList();
	}

	@Override
	public void createWishList(Long userId, CreateWishListRequest request) {
		Book book = bookRepository.findById(request.bookId())
			.orElseThrow(() -> new BookNotFoundException(request.bookId()));

		User user = userRepository.findById(userId)
			.orElseThrow(() -> new UserNotFoundException(userId));

		if (wishListRepository.existsByUserIdAndBookBookId(userId, book.getBookId())) {
			throw new WishListAlreadyExistsException(book.getBookId());
		}

		wishListRepository.save(new WishList(book, user));
	}

	@Override
	public void deleteWishList(Long wishListId) {
		wishListRepository.deleteById(wishListId);
	}
}
