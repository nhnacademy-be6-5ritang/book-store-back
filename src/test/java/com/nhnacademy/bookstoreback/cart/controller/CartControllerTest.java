// package com.nhnacademy.bookstoreback.cart.controller;
//
// import static org.mockito.ArgumentMatchers.*;
// import static org.mockito.Mockito.*;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
// import java.util.List;
//
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.mockito.Mock;
// import org.mockito.MockitoAnnotations;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
// import org.springframework.test.web.servlet.MockMvc;
// import org.springframework.test.web.servlet.setup.MockMvcBuilders;
// import org.springframework.web.context.WebApplicationContext;
//
// import com.fasterxml.jackson.databind.ObjectMapper;
// import com.nhnacademy.bookstoreback.auth.jwt.dto.CurrentUserDetails;
// import com.nhnacademy.bookstoreback.cart.domain.dto.response.GetCartResponse;
// import com.nhnacademy.bookstoreback.cart.service.CartService;
// import com.nhnacademy.bookstoreback.user.domain.dto.response.UserTokenInfo;
//
// import jakarta.servlet.http.HttpServletResponse;
//
// @WebMvcTest(CartController.class)
// class CartControllerTest {
//
// 	@Autowired
// 	private MockMvc mockMvc;
//
// 	@Mock
// 	private CartService cartService;
//
// 	private ObjectMapper objectMapper;
//
// 	@Mock
// 	private CurrentUserDetails currentUser;
//
// 	@Autowired
// 	private WebApplicationContext webApplicationContext;
//
// 	@BeforeEach
// 	void setUp() {
// 		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
// 		MockitoAnnotations.openMocks(this);
// 		mockMvc = MockMvcBuilders.standaloneSetup(new CartController(cartService)).build();
// 		objectMapper = new ObjectMapper();
// 		UserTokenInfo userTokenInfo = new UserTokenInfo(1L, "password", List.of("HEAD_ADMIN"), "ACTIVE");
// 		currentUser = new CurrentUserDetails(userTokenInfo);
// 	}
//
// 	@Test
// 	void testGetCart() throws Exception {
// 		String cartId = "e212cc9b-265f-4747-8c7d-4edbe573a480";
// 		GetCartResponse response = new GetCartResponse(cartId, currentUser.getUserId());
// 		when(cartService.getCart(cartId)).thenReturn(response);
//
// 		mockMvc.perform(get("/api/carts/{cartId}", cartId))
// 			.andExpect(status().isOk())
// 			.andExpect(content().json(objectMapper.writeValueAsString(response)));
// 	}
//
// 	@Test
// 	void testCreateCart() throws Exception {
// 		// Given
// 		HttpServletResponse resp = mock(HttpServletResponse.class);
//
// 		// When & Then
// 		mockMvc.perform(post("/api/carts")
// 				.header("Authorization",
// 					"Bearer some-jwt-token")) // Adjust according to how your @CurrentUser is configured
// 			.andExpect(status().isCreated());
//
// 		// Verify interaction with the service
// 		verify(cartService).createCart(any(CurrentUserDetails.class), any(HttpServletResponse.class));
// 	}
// }