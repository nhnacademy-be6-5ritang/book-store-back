package com.nhnacademy.bookstoreback.cart.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.RedisTemplate;

import com.nhnacademy.bookstoreback.auth.jwt.dto.CurrentUserDetails;
import com.nhnacademy.bookstoreback.bookcart.domain.entity.BookCart;
import com.nhnacademy.bookstoreback.bookcart.repository.BookCartRepository;
import com.nhnacademy.bookstoreback.cart.domain.dto.response.GetCartResponse;
import com.nhnacademy.bookstoreback.cart.domain.entity.Cart;
import com.nhnacademy.bookstoreback.cart.exception.CartAlreadyExistsException;
import com.nhnacademy.bookstoreback.cart.exception.CartNotFoundException;
import com.nhnacademy.bookstoreback.cart.repository.CartRepository;
import com.nhnacademy.bookstoreback.cart.service.impl.CartServiceImpl;
import com.nhnacademy.bookstoreback.user.domain.dto.response.UserTokenInfo;
import com.nhnacademy.bookstoreback.user.domain.entity.User;
import com.nhnacademy.bookstoreback.user.exception.UserNotFoundException;
import com.nhnacademy.bookstoreback.user.repository.UserRepository;

import jakarta.servlet.http.HttpServletResponse;

class CartServiceImplTest {

	@Mock
	private RedisTemplate<String, Object> cartRedisTemplate;

	@Mock
	private CartRepository cartRepository;

	@Mock
	private UserRepository userRepository;

	@Mock
	private BookCartRepository bookCartRepository;

	@InjectMocks
	private CartServiceImpl cartService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testGetCart() {
		String cartId = UUID.randomUUID().toString();
		Long userId = 1L;
		Cart cart = new Cart(userId);

		GetCartResponse expectedResponse = GetCartResponse.fromEntity(cart);

		when(cartRepository.findById(cartId)).thenReturn(Optional.of(cart));

		GetCartResponse response = cartService.getCart(cartId);

		assertEquals(expectedResponse, response);
	}

	@Test
	void testGetCartNotFound() {
		String cartId = UUID.randomUUID().toString();

		when(cartRepository.findById(cartId)).thenReturn(Optional.empty());

		assertThrows(CartNotFoundException.class, () -> cartService.getCart(cartId));
	}

	@Test
	void testCreateCartForGuest() {
		CurrentUserDetails currentUser = null;
		HttpServletResponse response = mock(HttpServletResponse.class);
		String cartId = UUID.randomUUID().toString();
		Cart cart = new Cart(null);

		when(cartRepository.save(any(Cart.class))).thenReturn(cart);
		when(bookCartRepository.save(any(BookCart.class))).thenReturn(null);

		Cart createdCart = cartService.createCart(currentUser, response);

		assertNotNull(createdCart);
		assertNotNull(createdCart.getCartId());
		assertEquals(36, createdCart.getCartId().length(), "Cart ID should be 36 characters long");
		verify(response).addCookie(any());
	}

	@Test
	void testCreateCartForUser() {
		Long userId = 1L;
		CurrentUserDetails currentUser = new CurrentUserDetails(UserTokenInfo.builder().id(userId).build());
		HttpServletResponse response = mock(HttpServletResponse.class);
		String cartId = UUID.randomUUID().toString();
		Cart cart = new Cart(userId);

		when(userRepository.findById(userId)).thenReturn(Optional.of(mock(User.class)));
		when(cartRepository.existsByUserId(userId)).thenReturn(false);
		when(cartRepository.save(any(Cart.class))).thenReturn(cart);
		when(bookCartRepository.save(any(BookCart.class))).thenReturn(null);

		Cart createdCart = cartService.createCart(currentUser, response);

		assertNotNull(createdCart);
		assertNotNull(createdCart.getCartId());
		assertEquals(36, createdCart.getCartId().length(), "Cart ID should be 36 characters long");
		verify(cartRepository).save(any(Cart.class));
	}

	@Test
	void testCreateCartWhenCartAlreadyExists() {
		Long userId = 1L;
		CurrentUserDetails currentUser = new CurrentUserDetails(UserTokenInfo.builder().id(userId).build());
		HttpServletResponse response = mock(HttpServletResponse.class);

		when(userRepository.findById(userId)).thenReturn(Optional.of(mock(User.class)));
		when(cartRepository.existsByUserId(userId)).thenReturn(true);

		assertThrows(CartAlreadyExistsException.class, () -> cartService.createCart(currentUser, response));
	}

	@Test
	void testCreateCartWhenUserNotFound() {
		Long userId = 1L;
		CurrentUserDetails currentUser = new CurrentUserDetails(UserTokenInfo.builder().id(userId).build());
		HttpServletResponse response = mock(HttpServletResponse.class);

		when(userRepository.findById(userId)).thenReturn(Optional.empty());

		assertThrows(UserNotFoundException.class, () -> cartService.createCart(currentUser, response));
	}
}