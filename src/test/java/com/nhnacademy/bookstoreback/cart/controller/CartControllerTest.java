package com.nhnacademy.bookstoreback.cart.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.bookstoreback.auth.jwt.dto.CurrentUserDetails;
import com.nhnacademy.bookstoreback.cart.domain.dto.response.GetCartResponse;
import com.nhnacademy.bookstoreback.cart.service.CartService;
import com.nhnacademy.bookstoreback.user.domain.dto.response.UserTokenInfo;

@WebMvcTest(CartController.class)
class CartControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private CartService cartService;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(new CartController(cartService)).build();
	}

	@Test
	void testGetCart() throws Exception {
		String cartId = "1";
		Long userId = 123L;
		GetCartResponse getCartResponse = GetCartResponse.builder()
			.cardId(cartId)
			.userId(userId)
			.build();

		when(cartService.getCart(cartId)).thenReturn(getCartResponse);

		mockMvc.perform(get("/api/carts/{cartId}", cartId)
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.cardId").value(cartId))
			.andExpect(jsonPath("$.userId").value(userId));
	}

	@Test
	void testCreateCart() throws Exception {
		Long userId = 123L;
		String password = "testPassword";
		List<String> roles = List.of("ROLE_USER");
		UserTokenInfo userTokenInfo = UserTokenInfo.builder()
			.id(userId)
			.password(password)
			.roles(roles)
			.status("ACTIVE")
			.build();

		CurrentUserDetails currentUserDetails = new CurrentUserDetails(userTokenInfo);

		mockMvc.perform(post("/api/carts")
				.contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", "Bearer someToken") // 필요에 따라 수정
				.content(new ObjectMapper().writeValueAsString(currentUserDetails)))
			.andExpect(status().isCreated());
	}
}