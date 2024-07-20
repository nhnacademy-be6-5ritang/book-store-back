package com.nhnacademy.bookstoreback.wishlist.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.nhnacademy.bookstoreback.auth.jwt.dto.CurrentUserDetails;
import com.nhnacademy.bookstoreback.author.domain.entity.Author;
import com.nhnacademy.bookstoreback.book.domain.entity.Book;
import com.nhnacademy.bookstoreback.book.domain.entity.BookImage;
import com.nhnacademy.bookstoreback.book.exception.BookNotFoundException;
import com.nhnacademy.bookstoreback.book.repository.BookRepository;
import com.nhnacademy.bookstoreback.image.domain.entity.Image;
import com.nhnacademy.bookstoreback.publisher.domain.entity.Publisher;
import com.nhnacademy.bookstoreback.user.domain.dto.response.UserTokenInfo;
import com.nhnacademy.bookstoreback.user.domain.entity.User;
import com.nhnacademy.bookstoreback.user.exception.UserNotFoundException;
import com.nhnacademy.bookstoreback.user.repository.UserRepository;
import com.nhnacademy.bookstoreback.wishlist.domain.dto.request.CreateWishListRequest;
import com.nhnacademy.bookstoreback.wishlist.domain.dto.response.GetWishListResponse;
import com.nhnacademy.bookstoreback.wishlist.domain.entity.WishList;
import com.nhnacademy.bookstoreback.wishlist.exception.WishListAlreadyExistsException;
import com.nhnacademy.bookstoreback.wishlist.exception.WishListNotFoundException;
import com.nhnacademy.bookstoreback.wishlist.repository.WishListRepository;
import com.nhnacademy.bookstoreback.wishlist.service.impl.WishListServiceImpl;

class WishListServiceImplTest {

	@InjectMocks
	private WishListServiceImpl wishListService;

	@Mock
	private WishListRepository wishListRepository;

	@Mock
	private BookRepository bookRepository;

	@Mock
	private UserRepository userRepository;

	private WishList wishList;
	private CurrentUserDetails currentUser;
	private User user;
	private Book book;
	private Author author;
	private Publisher publisher;
	private Image image;
	private BookImage bookImage;

	@BeforeEach
	void setup() {
		MockitoAnnotations.openMocks(this);
		UserTokenInfo userTokenInfo = new UserTokenInfo(1L, "password", Arrays.asList("HEAD_ADMIN"), "ACTIVE");
		currentUser = new CurrentUserDetails(userTokenInfo);
		user = mock(User.class);
		when(user.getId()).thenReturn(1L);

		author = mock(Author.class);
		publisher = mock(Publisher.class);

		book = mock(Book.class);
		when(book.getBookId()).thenReturn(1L);
		when(book.getBookTitle()).thenReturn("Book Title");
		when(book.getAuthor()).thenReturn(author);
		when(author.getAuthorName()).thenReturn("Author Name");
		when(book.getPublisher()).thenReturn(publisher);
		when(publisher.getPublisherName()).thenReturn("Publisher Name");
		when(book.getBookSalePrice()).thenReturn(BigDecimal.valueOf(100));
		when(book.getBookSalePercent()).thenReturn(BigDecimal.valueOf(10));

		image = mock(Image.class);
		when(image.getImageUrl()).thenReturn("http://example.com/image.jpg");

		bookImage = mock(BookImage.class);
		when(bookImage.getImage()).thenReturn(image);

		when(book.getBookImages()).thenReturn(Collections.singletonList(bookImage));

		wishList = mock(WishList.class);
		when(wishList.getWishListId()).thenReturn(1L);
		when(wishList.getBook()).thenReturn(book);
		when(wishList.getUser()).thenReturn(user);

	}

	@Test
	void testGetWishLists() {
		when(wishListRepository.findAllByUserId(1L)).thenReturn(Collections.singletonList(wishList));

		// When
		List<GetWishListResponse> responses = wishListService.getWishLists(currentUser);

		// Then
		assertNotNull(responses);
		assertEquals(1, responses.size());

		GetWishListResponse response = responses.get(0);
		assertEquals(1L, response.wishListId());
		assertEquals(1L, response.bookId());
		assertEquals("http://example.com/image.jpg", response.bookImageUrl());
		assertEquals("Book Title", response.bookTitle());
		assertEquals("Author Name", response.authorName());
		assertEquals("Publisher Name", response.publisherName());
		assertEquals(BigDecimal.valueOf(100), response.bookSalePrice());
		assertEquals(BigDecimal.valueOf(10), response.bookSalePercent());
	}

	@Test
	void testCreateWishList() {
		CreateWishListRequest request = new CreateWishListRequest(book.getBookId());
		wishList = new WishList(book, user);

		when(bookRepository.findById(book.getBookId())).thenReturn(Optional.of(book));
		when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
		when(wishListRepository.existsByUserIdAndBookBookId(wishList.getBook().getBookId(),
			wishList.getUser().getId())).thenReturn(false);

		wishListService.createWishList(currentUser, request);

		verify(wishListRepository).save(any(WishList.class));
	}

	@Test
	void testCreateWishList_AlreadyExists() {
		CreateWishListRequest request = new CreateWishListRequest(book.getBookId());
		Long userId = 1L;
		Long bookId = 1L;

		when(bookRepository.findById(1L)).thenReturn(java.util.Optional.of(book));
		when(userRepository.findById(1L)).thenReturn(java.util.Optional.of(user));
		when(wishListRepository.existsByUserIdAndBookBookId(userId, bookId)).thenReturn(true);

		// 실행 및 검증
		assertThrows(WishListAlreadyExistsException.class, () -> {
			wishListService.createWishList(currentUser, request);
		});
	}

	@Test
	void testDeleteWishList() {
		wishList = new WishList(book, user);
		when(wishListRepository.findById(1L)).thenReturn(Optional.of(wishList));

		wishListService.deleteWishList(1L, currentUser);

	}

	@Test
	void testDeleteWishList_NotOwnedByUser() {
		WishList wishList = new WishList(book, user);

		when(wishListRepository.findById(1L)).thenReturn(java.util.Optional.of(wishList));
		when(wishList.getUser().getId()).thenReturn(2L);

		wishListService.deleteWishList(1L, currentUser);

		verify(wishListRepository, never()).deleteById(1L);
	}

	@Test
	void testCreateWishList_NoCurrentUser() {
		CreateWishListRequest request = mock(CreateWishListRequest.class);

		assertThrows(UserNotFoundException.class, () -> wishListService.createWishList(null, request));
	}

	@Test
	void testGetWishLists_NoCurrentUser() {
		assertThrows(UserNotFoundException.class, () -> wishListService.getWishLists(null));
	}

	@Test
	void testCreateWishList_BookNotFound() {
		CreateWishListRequest request = mock(CreateWishListRequest.class);

		when(bookRepository.findById(anyLong())).thenReturn(java.util.Optional.empty());

		assertThrows(BookNotFoundException.class, () -> wishListService.createWishList(currentUser, request));
	}

	@Test
	void testCreateWishList_UserNotFound() {
		CreateWishListRequest request = mock(CreateWishListRequest.class);
		Book book = mock(Book.class);

		when(bookRepository.findById(anyLong())).thenReturn(java.util.Optional.of(book));
		when(userRepository.findById(anyLong())).thenReturn(java.util.Optional.empty());

		assertThrows(UserNotFoundException.class,
			() -> wishListService.createWishList(mock(CurrentUserDetails.class), request));
	}

	@Test
	void testDeleteWishList_WishListNotFound() {
		assertThrows(WishListNotFoundException.class, () -> wishListService.deleteWishList(1L, currentUser));
	}
}