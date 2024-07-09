package com.nhnacademy.bookstoreback.wishlist.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nhnacademy.bookstoreback.auth.jwt.dto.CurrentUserDetails;
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
import com.nhnacademy.bookstoreback.wishlist.exception.WishListNotFoundException;
import com.nhnacademy.bookstoreback.wishlist.repository.WishListRepository;
import com.nhnacademy.bookstoreback.wishlist.service.WishListService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class WishListServiceImpl implements WishListService {
	private final WishListRepository wishListRepository;
	private final BookRepository bookRepository;
	private final UserRepository userRepository;

	@Transactional(readOnly = true)
	@Override
	public List<GetWishListResponse> getWishLists(CurrentUserDetails currentUser) {
		Long userId = currentUser != null ? currentUser.getUserId() : null;
		if (userId == null) {
			throw new UserNotFoundException(0L);
		}

		return wishListRepository.findAllByUserId(userId).stream()
			.map(GetWishListResponse::fromEntity).toList();
	}

	@Override
	public void createWishList(CurrentUserDetails currentUser, CreateWishListRequest request) {
		Long userId = currentUser != null ? currentUser.getUserId() : null;

		if (userId == null) {
			throw new UserNotFoundException(0L);
		}

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
	public void deleteWishList(Long wishListId, CurrentUserDetails currentUser) {
		Long userId = currentUser != null ? currentUser.getUserId() : null;

		if (userId == null) {
			throw new UserNotFoundException(0L);
		} else {
			WishList wishList = wishListRepository.findById(wishListId)
				.orElseThrow(() -> new WishListNotFoundException(wishListId));

			// 해당 회원의 위시리스트 아이디와 실제 위시리스트가 가진 회원 아이디가 일치할 경우만 삭제되도록
			if (wishList.getUser().getId().equals(userId)) {
				wishListRepository.deleteById(wishListId);
			}
		}

	}
}
