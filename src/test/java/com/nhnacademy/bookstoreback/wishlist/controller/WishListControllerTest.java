package com.nhnacademy.bookstoreback.wishlist.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.bookstoreback.wishlist.domain.dto.request.CreateWishListRequest;
import com.nhnacademy.bookstoreback.wishlist.service.WishListService;

@WebMvcTest(WishListController.class)
class WishListControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext webApplicationContext;

	@MockBean
	private WishListService wishListService;

	@Autowired
	private ObjectMapper objectMapper;

	@BeforeEach
	void setUp() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}

	@Test
	void testGetWishLists() throws Exception {
		when(wishListService.getWishLists(any())).thenReturn(Collections.emptyList());

		mockMvc.perform(get("/api/wishLists"))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.length()").value(0));
	}

	@Test
	void testCreateWishList() throws Exception {
		CreateWishListRequest request = mock(CreateWishListRequest.class);

		doNothing().when(wishListService).createWishList(any(), any(CreateWishListRequest.class));

		mockMvc.perform(post("/api/wishLists")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isCreated());
	}

	@Test
	void testDeleteWishList() throws Exception {
		doNothing().when(wishListService).deleteWishList(eq(1L), any());

		mockMvc.perform(delete("/api/wishLists/1"))
			.andExpect(status().isOk());
	}
}
