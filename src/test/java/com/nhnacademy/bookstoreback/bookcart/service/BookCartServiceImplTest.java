package com.nhnacademy.bookstoreback.bookcart.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.RedisTemplate;

import com.nhnacademy.bookstoreback.auth.jwt.dto.CurrentUserDetails;
import com.nhnacademy.bookstoreback.author.domain.entity.Author;
import com.nhnacademy.bookstoreback.book.domain.entity.Book;
import com.nhnacademy.bookstoreback.book.domain.entity.BookImage;
import com.nhnacademy.bookstoreback.book.repository.BookRepository;
import com.nhnacademy.bookstoreback.bookcart.domain.dto.request.CreateBookCartRequest;
import com.nhnacademy.bookstoreback.bookcart.domain.dto.request.UpdateBookCartRequest;
import com.nhnacademy.bookstoreback.bookcart.domain.dto.response.GetBookCartResponse;
import com.nhnacademy.bookstoreback.bookcart.domain.entity.BookBundle;
import com.nhnacademy.bookstoreback.bookcart.domain.entity.BookCart;
import com.nhnacademy.bookstoreback.bookcart.exception.BookCartAlreadyExistsException;
import com.nhnacademy.bookstoreback.bookcart.repository.BookCartRepository;
import com.nhnacademy.bookstoreback.bookcart.service.impl.BookCartServiceImpl;
import com.nhnacademy.bookstoreback.cart.domain.entity.Cart;
import com.nhnacademy.bookstoreback.cart.repository.CartRepository;
import com.nhnacademy.bookstoreback.cart.service.CartService;
import com.nhnacademy.bookstoreback.image.domain.entity.Image;
import com.nhnacademy.bookstoreback.publisher.domain.entity.Publisher;
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
	private Book book;
	private Author author;
	private Publisher publisher;
	private Image image;
	private BookImage bookImage;
	private Cart cart;
	private BookCart bookCart;
	private long ttl = 3600; // 1 hour in seconds

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		currentUser = new CurrentUserDetails(new UserTokenInfo(1L, "password", Collections.emptyList(), "ACTIVE"));
		book = mock(Book.class);
		author = mock(Author.class);
		publisher = mock(Publisher.class);
		image = mock(Image.class);
		bookImage = mock(BookImage.class);
		cart = mock(Cart.class);
		bookCart = new BookCart(cartId, null);

		when(book.getBookId()).thenReturn(1L);
		when(book.getBookTitle()).thenReturn("Book Title");
		when(book.getAuthor()).thenReturn(author);
		when(author.getAuthorName()).thenReturn("Author Name");
		when(book.getPublisher()).thenReturn(publisher);
		when(publisher.getPublisherName()).thenReturn("Publisher Name");
		when(book.getBookSalePrice()).thenReturn(BigDecimal.valueOf(100));
		when(book.getBookSalePercent()).thenReturn(BigDecimal.valueOf(10));

		when(image.getImageUrl()).thenReturn("http://example.com/image.jpg");
		when(bookImage.getImage()).thenReturn(image);
		when(book.getBookImages()).thenReturn(Collections.singletonList(bookImage));

		when(cartService.createCart(any(CurrentUserDetails.class), any(HttpServletResponse.class)))
			.thenReturn(cart);
		when(cart.getCartId()).thenReturn(cartId);
	}

	@Test
	void testGetBookCartsByCartId() {
		BookBundle bookBundle = new BookBundle(1L, 10);
		List<BookBundle> bookBundles = new ArrayList<>();
		bookBundles.add(bookBundle);
		BookCart bookCart = new BookCart(cartId, bookBundles);

		when(bookCartRepository.findById(cartId)).thenReturn(Optional.of(bookCart));
		when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

		GetBookCartResponse response = GetBookCartResponse.fromEntity(book, 10, cartId);

		List<GetBookCartResponse> expectedResponses = Collections.singletonList(response);
		List<GetBookCartResponse> actualResponses = bookCartService.getBookCartsByCartId(currentUser, cartId);

		assertThat(actualResponses).isEqualTo(expectedResponses);
		verify(bookCartRepository).findById(cartId);
	}

	@Test
	void testCreateBookCart() {
		CreateBookCartRequest request = new CreateBookCartRequest(1L, 2);
		List<BookBundle> bookBundles = new ArrayList<>();
		BookCart bookCart = new BookCart(cartId, bookBundles);

		when(bookCartRepository.findById(cartId)).thenReturn(Optional.of(bookCart));
		when(bookRepository.findById(request.bookId())).thenReturn(Optional.of(book));

		bookCartService.createBookCart(currentUser, request, cartId);

		verify(bookCartRepository).save(any(BookCart.class));
	}

	@Test
	void testCreateBookCart_WhenBookAlreadyExists() {
		CreateBookCartRequest request = new CreateBookCartRequest(1L, 2);
		List<BookBundle> bookBundles = new ArrayList<>();
		bookBundles.add(new BookBundle(1L, 10));
		BookCart bookCart = new BookCart(cartId, bookBundles);

		when(bookCartRepository.findById(cartId)).thenReturn(Optional.of(bookCart));
		when(bookRepository.findById(request.bookId())).thenReturn(Optional.of(book));

		assertThatThrownBy(() -> bookCartService.createBookCart(currentUser, request, cartId))
			.isInstanceOf(BookCartAlreadyExistsException.class);
	}

	@Test
	void testUpdateBookCart() {
		UpdateBookCartRequest request = new UpdateBookCartRequest(5);
		List<BookBundle> bookBundles = new ArrayList<>();
		bookBundles.add(new BookBundle(1L, 10));
		BookCart bookCart = new BookCart(cartId, bookBundles);

		when(bookCartRepository.findById(cartId)).thenReturn(Optional.of(bookCart));

		bookCartService.updateBookCart(1L, currentUser, request, cartId);

		verify(bookCartRepository).save(any(BookCart.class));
	}

	@Test
	void testDeleteBookCart() {
		List<BookBundle> bookBundles = new ArrayList<>();
		bookBundles.add(new BookBundle(1L, 10));
		BookCart bookCart = new BookCart(cartId, bookBundles);

		when(bookCartRepository.findById(cartId)).thenReturn(Optional.of(bookCart));

		bookCartService.deleteBookCart(1L, currentUser, cartId);

		verify(bookCartRepository).save(any(BookCart.class));
	}

	@Test
	void testSetupCart_NoCartIdForGuest() {
		CurrentUserDetails guestUserDetails = new CurrentUserDetails(
			new UserTokenInfo(null, "password", Collections.emptyList(), "ACTIVE"));

		when(cartRepository.findById(anyString())).thenReturn(Optional.empty());

		String resultCartId = bookCartService.setupCart(guestUserDetails, "");

		assertThat(resultCartId).isEqualTo(cartId);
		verify(cartService).createCart(guestUserDetails, resp);
	}

	@Test
	void testSetupCart_HasCartIdForGuest() {
		CurrentUserDetails guestUserDetails = new CurrentUserDetails(
			new UserTokenInfo(null, "password", Collections.emptyList(), "ACTIVE")
		);

		String resultCartId = bookCartService.setupCart(guestUserDetails, cartId);

		assertThat(resultCartId).isEqualTo(cartId);
		verify(cartService, never()).createCart(any(CurrentUserDetails.class), any(HttpServletResponse.class));
	}

	@Test
	void testSetupCart_NoCartForUser() {
		when(cartRepository.existsByUserId(1L)).thenReturn(false);
		when(cartRepository.findByUserId(1L)).thenReturn(Optional.empty());

		String resultCartId = bookCartService.setupCart(currentUser, "");

		assertThat(resultCartId).isEqualTo(cartId);
		verify(cartService).createCart(currentUser, resp);
	}

	@Test
	void testSetupCart_HasCartForUser() {
		when(cartRepository.existsByUserId(1L)).thenReturn(true);
		when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(cart));

		String resultCartId = bookCartService.setupCart(currentUser, "");

		assertThat(resultCartId).isEqualTo(cartId);
		verify(cartService, never()).createCart(any(CurrentUserDetails.class), any(HttpServletResponse.class));
	}

	@Test
	void testSaveWithTtl_ExistingTTL() {
		when(cartRedisTemplate.getExpire(anyString(), any(TimeUnit.class))).thenReturn(ttl);

		bookCartService.saveWithTtl(bookCart);

		verify(bookCartRepository).save(bookCart);
		verify(cartRedisTemplate).expire(anyString(), eq(ttl), eq(TimeUnit.SECONDS));
	}

	@Test
	void testSaveWithTtl_NoTTL() {
		when(cartRedisTemplate.getExpire(anyString(), any(TimeUnit.class))).thenReturn(null);

		bookCartService.saveWithTtl(bookCart);

		verify(bookCartRepository).save(bookCart);
		verify(cartRedisTemplate, never()).expire(anyString(), anyLong(), any(TimeUnit.class));
	}
}
