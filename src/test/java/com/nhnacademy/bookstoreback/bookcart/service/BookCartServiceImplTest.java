package com.nhnacademy.bookstoreback.bookcart.service;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.RedisTemplate;

import com.nhnacademy.bookstoreback.auth.jwt.dto.CurrentUserDetails;
import com.nhnacademy.bookstoreback.book.repository.BookRepository;
import com.nhnacademy.bookstoreback.bookcart.domain.dto.request.CreateBookCartRequest;
import com.nhnacademy.bookstoreback.bookcart.domain.dto.request.UpdateBookCartRequest;
import com.nhnacademy.bookstoreback.bookcart.domain.dto.response.GetBookCartResponse;
import com.nhnacademy.bookstoreback.bookcart.domain.entity.Book;
import com.nhnacademy.bookstoreback.bookcart.domain.entity.BookCart;
import com.nhnacademy.bookstoreback.bookcart.exception.BookCartAlreadyExistsException;
import com.nhnacademy.bookstoreback.bookcart.repository.BookCartRepository;
import com.nhnacademy.bookstoreback.bookcart.service.impl.BookCartServiceImpl;
import com.nhnacademy.bookstoreback.cart.repository.CartRepository;
import com.nhnacademy.bookstoreback.cart.service.CartService;
import com.nhnacademy.bookstoreback.user.domain.dto.response.UserTokenInfo;

import jakarta.servlet.http.HttpServletResponse;

class BookCartServiceImplTest {

	@Mock
	private BookRepository bookRepository;

	@Mock
	private CartRepository cartRepository;

	@Mock
	private CartService cartService;

	@Mock
	private HttpServletResponse resp;

	@Mock
	private BookCartRepository bookCartRepository;

	@Mock
	private RedisTemplate<String, Object> cartRedisTemplate;

	@InjectMocks
	private BookCartServiceImpl bookCartService;

	private CurrentUserDetails currentUser;
	private String cartId = "e212cc9b-265f-4747-8c7d-4edbe573a480";
	private com.nhnacademy.bookstoreback.book.domain.entity.Book book2 = mock(
		com.nhnacademy.bookstoreback.book.domain.entity.Book.class);

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		currentUser = new CurrentUserDetails(new UserTokenInfo(1L, "password", Collections.emptyList(), "ACTIVE"));
	}

	@Test
	void testGetBookCartsByCartId() {
		Book book = new Book(1L, 10);
		BookCart bookCart = new BookCart(cartId, Collections.singletonList(book));
		when(bookCartRepository.findById(cartId)).thenReturn(Optional.of(bookCart));
		when(bookRepository.findById(1L)).thenReturn(Optional.of(book2));

		GetBookCartResponse response = GetBookCartResponse.fromEntity(book, 2, cartId);

		List<GetBookCartResponse> expectedResponses = Collections.singletonList(response);
		List<GetBookCartResponse> actualResponses = bookCartService.getBookCartsByCartId(currentUser, cartId);

		assertThat(actualResponses).isEqualTo(expectedResponses);
		verify(bookCartRepository).findById(cartId);
	}

	@Test
	void testCreateBookCart() {
		CreateBookCartRequest request = new CreateBookCartRequest(1L, 2);
		Book book = new Book(1L, 10);
		BookCart bookCart = new BookCart(cartId, Collections.emptyList());

		when(bookCartRepository.findById(cartId)).thenReturn(Optional.of(bookCart));
		when(bookRepository.findById(request.bookId())).thenReturn(Optional.of(book));

		bookCartService.createBookCart(currentUser, request, cartId);

		verify(bookCartRepository).save(any(BookCart.class));
	}

	@Test
	void testCreateBookCart_WhenBookAlreadyExists() {
		CreateBookCartRequest request = new CreateBookCartRequest(1L, 2);
		Book book = new Book(1L, 10);
		BookCart bookCart = new BookCart(cartId, Collections.singletonList(book));

		when(bookCartRepository.findById(cartId)).thenReturn(Optional.of(bookCart));
		when(bookRepository.findById(request.bookId())).thenReturn(Optional.of(book));

		assertThatThrownBy(() -> bookCartService.createBookCart(currentUser, request, cartId))
			.isInstanceOf(BookCartAlreadyExistsException.class);
	}

	@Test
	void testUpdateBookCart() {
		UpdateBookCartRequest request = new UpdateBookCartRequest(5);
		Book book = new Book(1L, 10);
		BookCart bookCart = new BookCart(cartId, Collections.singletonList(book));

		when(bookCartRepository.findById(cartId)).thenReturn(Optional.of(bookCart));

		bookCartService.updateBookCart(1L, currentUser, request, cartId);

		verify(bookCartRepository).save(any(BookCart.class));
	}

	@Test
	void testDeleteBookCart() {
		Book book = new Book(1L, 10);
		BookCart bookCart = new BookCart(cartId, Collections.singletonList(book));

		when(bookCartRepository.findById(cartId)).thenReturn(Optional.of(bookCart));

		bookCartService.deleteBookCart(1L, currentUser, cartId);

		verify(bookCartRepository).save(any(BookCart.class));
	}
}