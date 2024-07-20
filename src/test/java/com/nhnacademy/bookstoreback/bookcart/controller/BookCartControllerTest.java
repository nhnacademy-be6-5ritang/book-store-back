package com.nhnacademy.bookstoreback.bookcart.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.bookstoreback.auth.jwt.dto.CurrentUserDetails;
import com.nhnacademy.bookstoreback.bookcart.domain.dto.request.CreateBookCartRequest;
import com.nhnacademy.bookstoreback.bookcart.domain.dto.request.UpdateBookCartRequest;
import com.nhnacademy.bookstoreback.bookcart.service.BookCartService;
import com.nhnacademy.bookstoreback.user.domain.dto.response.UserTokenInfo;

import jakarta.servlet.http.Cookie;

@WebMvcTest(BookCartController.class)
class BookCartControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private BookCartService bookCartService;

	@Autowired
	private ObjectMapper objectMapper;

	private CurrentUserDetails currentUser;
	private String cartId;

	@BeforeEach
	void setUp() {
		UserTokenInfo userTokenInfo = new UserTokenInfo(1L, "password", Arrays.asList("HEAD_ADMIN"), "ACTIVE");
		currentUser = new CurrentUserDetails(userTokenInfo);
		cartId = "693eabda-9a8d-4f89-b8fd-7ba7e56f59ce";
	}

	@Test
	void testGetBookCarts() throws Exception {
		when(bookCartService.getBookCartsByCartId(currentUser, cartId)).thenReturn(Collections.emptyList());

		mockMvc.perform(get("/api/carts/me")
				.cookie(new Cookie("cartId", cartId))
				.requestAttr("currentUser", currentUser))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$").isArray())
			.andExpect(jsonPath("$").isEmpty());

		verify(bookCartService, times(1)).getBookCartsByCartId(currentUser, cartId);
	}

	@Test
	void testCreateBookCart() throws Exception {
		CreateBookCartRequest request = new CreateBookCartRequest(1L, 1);
		// set necessary fields for request

		mockMvc.perform(post("/api/carts/me")
				.cookie(new Cookie("cartId", cartId))
				.requestAttr("currentUser", currentUser)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isCreated());

		verify(bookCartService, times(1)).createBookCart(eq(currentUser), eq(request), eq(cartId));
	}

	@Test
	void testUpdateBookCart() throws Exception {
		UpdateBookCartRequest request = new UpdateBookCartRequest(1);
		// set necessary fields for request
		Long bookId = 1L;

		mockMvc.perform(put("/api/carts/me/{bookId}", bookId)
				.cookie(new Cookie("cartId", cartId))
				.requestAttr("currentUser", currentUser)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isOk());

		verify(bookCartService, times(1)).updateBookCart(eq(bookId), eq(currentUser), eq(request), eq(cartId));
	}

	@Test
	void testDeleteBookCart() throws Exception {
		Long bookId = 1L;

		mockMvc.perform(delete("/api/carts/me/{bookId}", bookId)
				.cookie(new Cookie("cartId", cartId))
				.requestAttr("currentUser", currentUser))
			.andExpect(status().isOk());

		verify(bookCartService, times(1)).deleteBookCart(eq(bookId), eq(currentUser), eq(cartId));
	}
}
