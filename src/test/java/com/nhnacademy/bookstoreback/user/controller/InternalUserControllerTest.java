package com.nhnacademy.bookstoreback.user.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.bookstoreback.user.domain.dto.response.GetPaycoUserTokenInfoResponse;
import com.nhnacademy.bookstoreback.user.domain.dto.response.UserTokenInfo;
import com.nhnacademy.bookstoreback.user.service.UserService;

@WebMvcTest(InternalUserController.class)
@AutoConfigureMockMvc(addFilters = false)
public class InternalUserControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserService userService;

	@Autowired
	private ObjectMapper objectMapper;

	private UserTokenInfo userTokenInfo;
	private GetPaycoUserTokenInfoResponse paycoUserTokenInfoResponse;

	@BeforeEach
	void setUp() {
		userTokenInfo = UserTokenInfo.builder()
			.id(1L)
			.password("password")
			.roles(List.of("ROLE_MEMBER"))
			.status("ACTIVE")
			.build();

		paycoUserTokenInfoResponse = GetPaycoUserTokenInfoResponse.builder()
			.id(1L)
			.roles(List.of("ROLE_MEMBER"))
			.status("ACTIVE")
			.build();
	}

	@Test
	void getUserInfoByEmail() throws Exception {
		when(userService.getUserTokenInfoByEmail(anyString())).thenReturn(userTokenInfo);

		mockMvc.perform(get("/api/internal/users/info")
				.header("X-User-Email", "test@example.com")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.id").value(userTokenInfo.id()))
			.andExpect(jsonPath("$.password").value(userTokenInfo.password()))
			.andExpect(jsonPath("$.roles[0]").value("ROLE_MEMBER"))
			.andExpect(jsonPath("$.status").value(userTokenInfo.status()));
	}

	@Test
	void getUserInfoByEmail_NotFound() throws Exception {
		when(userService.getUserTokenInfoByEmail(anyString())).thenReturn(null);

		mockMvc.perform(get("/api/internal/users/info")
				.header("X-User-Email", "nonexistent@example.com")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isNotFound());
	}

	@Test
	void getUserInfoByPaycoId() throws Exception {
		when(userService.getUserTokenInfoByPaycoId(anyString())).thenReturn(paycoUserTokenInfoResponse);

		mockMvc.perform(get("/api/internal/users/info-by-payco-id")
				.param("paycoIdNo", "123456")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.id").value(paycoUserTokenInfoResponse.id()))
			.andExpect(jsonPath("$.roles[0]").value("ROLE_MEMBER"))
			.andExpect(jsonPath("$.status").value(paycoUserTokenInfoResponse.status()));
	}

	@Test
	void getUserInfoByPaycoId_NotFound() throws Exception {
		when(userService.getUserTokenInfoByPaycoId(anyString())).thenReturn(null);

		mockMvc.perform(get("/api/internal/users/info-by-payco-id")
				.param("paycoIdNo", "nonexistent")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isNotFound());
	}
}
