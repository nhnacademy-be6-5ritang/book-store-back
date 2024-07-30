package com.nhnacademy.bookstoreback.bookcart.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.bookstoreback.auth.jwt.dto.CurrentUserDetails;
import com.nhnacademy.bookstoreback.bookcart.domain.dto.request.CreateBookCartRequest;
import com.nhnacademy.bookstoreback.bookcart.domain.dto.request.UpdateBookCartRequest;
import com.nhnacademy.bookstoreback.bookcart.domain.dto.response.GetBookCartResponse;
import com.nhnacademy.bookstoreback.bookcart.service.BookCartService;
import com.nhnacademy.bookstoreback.user.domain.dto.response.UserTokenInfo;

import jakarta.servlet.http.Cookie;

@WebMvcTest(BookCartController.class)
class BookCartControllerTest {

	private MockMvc mockMvc;

	@MockBean
	private BookCartService bookCartService;

	private ObjectMapper objectMapper;
	private CurrentUserDetails currentUser;
	private final String cartId = "e212cc9b-265f-4747-8c7d-4edbe573a480";

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(new BookCartController(bookCartService))
			.build();
		objectMapper = new ObjectMapper();
		UserTokenInfo userTokenInfo = new UserTokenInfo(1L, "password", Arrays.asList("HEAD_ADMIN"), "ACTIVE");
		currentUser = new CurrentUserDetails(userTokenInfo);

	}

	@Test
	void testGetBookCarts() throws Exception {
		List<GetBookCartResponse> responses = Collections.emptyList();
		when(bookCartService.getBookCartsByCartId(any(CurrentUserDetails.class), eq(cartId))).thenReturn(responses);

		mockMvc.perform(get("/api/carts/me")
				.cookie(new Cookie("cartId", cartId)))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(content().json(objectMapper.writeValueAsString(responses)));

		verify(bookCartService).getBookCartsByCartId(any(CurrentUserDetails.class), eq(cartId));
	}

	@Test
	void testCreateBookCart() throws Exception {
		CreateBookCartRequest request = new CreateBookCartRequest(1L, 2);

		mockMvc.perform(post("/api/carts/me")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))
				.cookie(new Cookie("cartId", cartId)))
			.andExpect(status().isCreated());

		verify(bookCartService).createBookCart(any(CurrentUserDetails.class), eq(request),
			any(String.class));
	}

	@Test
	void testUpdateBookCart() throws Exception {
		UpdateBookCartRequest request = new UpdateBookCartRequest(3);

		mockMvc.perform(put("/api/carts/me/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))
				.cookie(new Cookie("cartId", cartId)))
			.andExpect(status().isOk());

		verify(bookCartService).updateBookCart(any(Long.class), any(CurrentUserDetails.class),
			eq(request), eq(cartId));
	}

	@Test
	void testDeleteBookCart() throws Exception {
		mockMvc.perform(delete("/api/carts/me/1")
				.cookie(new Cookie("cartId", cartId)))
			.andExpect(status().isOk());

		verify(bookCartService).deleteBookCart(any(Long.class), any(CurrentUserDetails.class), eq(cartId));
	}
}
