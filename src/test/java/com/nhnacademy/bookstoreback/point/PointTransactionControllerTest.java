package com.nhnacademy.bookstoreback.point;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.bookstoreback.auth.jwt.dto.CurrentUserDetails;
import com.nhnacademy.bookstoreback.point.transaction.controller.PointTransactionController;
import com.nhnacademy.bookstoreback.point.transaction.domain.dto.response.GetPointTransactionResponse;
import com.nhnacademy.bookstoreback.point.transaction.service.PointTransactionService;

@WebMvcTest(PointTransactionController.class)
class PointTransactionControllerTest {

	private MockMvc mockMvc;

	@MockBean
	private PointTransactionService pointTransactionService;

	private ObjectMapper objectMapper;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(new PointTransactionController(pointTransactionService)).build();
		objectMapper = new ObjectMapper();
	}

	@Test
	@WithMockUser(roles = "MEMBER")
	void generalReviewPointTransaction() throws Exception {
		GetPointTransactionResponse response = GetPointTransactionResponse.builder()
			.pointEarningPolicyType("REVIEW")
			.pointTransactionAmount(new BigDecimal("50.00"))
			.build();

		when(pointTransactionService.reviewPointTransaction(any(CurrentUserDetails.class), anyString()))
			.thenReturn(response);

		mockMvc.perform(post("/api/point-transactions/reviews")
				.param("reviewType", "positive")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isCreated())
			.andExpect(content().json("{"
				+ "\"pointEarningPolicyType\":\"REVIEW\","
				+ "\"pointTransactionAmount\":50.00"
				+ "}"));
	}
}
