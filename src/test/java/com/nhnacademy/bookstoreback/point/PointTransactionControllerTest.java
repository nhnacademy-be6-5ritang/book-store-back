package com.nhnacademy.bookstoreback.point;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.bookstoreback.auth.jwt.dto.CurrentUserDetails;
import com.nhnacademy.bookstoreback.point.transaction.controller.PointTransactionController;
import com.nhnacademy.bookstoreback.point.transaction.domain.dto.response.GetAllPointTransactionResponse;
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
		mockMvc = MockMvcBuilders.standaloneSetup(new PointTransactionController(pointTransactionService))
			.setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
			.build();
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

	@Test
	@WithMockUser(roles = "MEMBER")
	void getPointTransactions() throws Exception {
		GetPointTransactionResponse response = GetPointTransactionResponse.builder()
			.pointEarningPolicyType("REVIEW")
			.pointTransactionAmount(new BigDecimal("50.00"))
			.build();

		Page<GetPointTransactionResponse> page = new PageImpl<>(List.of(response), PageRequest.of(0, 10), 1);

		when(pointTransactionService.getPointTransactions(any(CurrentUserDetails.class), any(Pageable.class)))
			.thenReturn(page);

		mockMvc.perform(get("/api/point-transactions")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.content[0].pointEarningPolicyType").value("REVIEW"))
			.andExpect(jsonPath("$.content[0].pointTransactionAmount").value(50.00));
	}

	@Test
	void getAllPointTransactions() throws Exception {
		GetAllPointTransactionResponse response = GetAllPointTransactionResponse.builder()
			.userId(1L)
			.policyId(2L)
			.amount(new BigDecimal("100.00"))
			.date(LocalDateTime.now())
			.build();

		Page<GetAllPointTransactionResponse> page = new PageImpl<>(List.of(response), PageRequest.of(0, 10), 1);

		when(pointTransactionService.getAllPointTransaction(any(Pageable.class)))
			.thenReturn(page);

		mockMvc.perform(get("/api/point-transactions/all")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.content[0].amount").value(100.00))
			.andExpect(jsonPath("$.content[0].userId").value(1))
			.andExpect(jsonPath("$.content[0].policyId").value(2))
			.andExpect(jsonPath("$.content[0].date").exists());
	}
}