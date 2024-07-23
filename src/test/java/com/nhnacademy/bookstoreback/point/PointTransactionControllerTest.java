package com.nhnacademy.bookstoreback.point;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
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
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.bookstoreback.auth.jwt.dto.CurrentUserDetails;
import com.nhnacademy.bookstoreback.point.transaction.controller.PointTransactionController;
import com.nhnacademy.bookstoreback.point.transaction.domain.dto.response.GetAllPointTransactionResponse;
import com.nhnacademy.bookstoreback.point.transaction.domain.dto.response.GetPointTransactionResponse;
import com.nhnacademy.bookstoreback.point.transaction.service.PointTransactionService;
import com.nhnacademy.bookstoreback.user.domain.dto.response.UserTokenInfo;

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
			.setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver()).build();
		objectMapper = new ObjectMapper();
	}

	@Test
	void testGetPointTransactions() throws Exception {
		Long userId = 1L;
		String policyType = "REVIEW";
		BigDecimal amount = BigDecimal.valueOf(50.00);
		LocalDateTime date = LocalDateTime.now();

		GetPointTransactionResponse response = GetPointTransactionResponse.builder()
			.pointEarningPolicyType(policyType)
			.pointTransactionAmount(amount)
			.pointTransactionDate(date)
			.build();

		Page<GetPointTransactionResponse> page = new PageImpl<>(List.of(response),
			PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "pointTransactionDate")), 1);

		UserTokenInfo userTokenInfo = UserTokenInfo.builder()
			.id(userId)
			.password("password123")
			.roles(List.of("ROLE_USER", "ROLE_MEMBER"))
			.status("ACTIVE")
			.build();
		CurrentUserDetails currentUser = new CurrentUserDetails(userTokenInfo);

		when(pointTransactionService.getPointTransactions(any(CurrentUserDetails.class), any(Pageable.class)))
			.thenReturn(page);

		mockMvc.perform(get("/api/point-transactions")
				.param("page", "0")
				.param("size", "10")
				.param("sort", "pointTransactionDate,desc")
				.with(user(currentUser)) // Mock CurrentUserDetails
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.content[0].pointEarningPolicyType").value(policyType))
			.andExpect(jsonPath("$.content[0].pointTransactionAmount").value(amount.doubleValue()));
	}

	@Test
	void testGetAllPointTransactions() throws Exception {
		Long userId = 1L;
		Long policyId = 1L;
		BigDecimal amount = BigDecimal.valueOf(50.00);
		LocalDateTime date = LocalDateTime.now();

		GetAllPointTransactionResponse response = GetAllPointTransactionResponse.builder()
			.userId(userId)
			.policyId(policyId)
			.amount(amount)
			.date(date)
			.build();

		Page<GetAllPointTransactionResponse> page = new PageImpl<>(List.of(response),
			PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "pointTransactionDate")), 1);

		when(pointTransactionService.getAllPointTransaction(any(Pageable.class)))
			.thenReturn(page);

		// When & Then
		mockMvc.perform(get("/api/point-transactions/all")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.content[0].userId").value(userId))
			.andExpect(jsonPath("$.content[0].policyId").value(policyId))
			.andExpect(jsonPath("$.content[0].amount").value(amount.doubleValue()));
	}

	@Test
	void testReviewPointTransaction() throws Exception {
		UserTokenInfo userTokenInfo = UserTokenInfo.builder()
			.id(1L)
			.password("password123")
			.roles(List.of("ROLE_USER", "ROLE_MEMBER"))
			.status("ACTIVE")
			.build();
		CurrentUserDetails currentUser = new CurrentUserDetails(userTokenInfo);

		mockMvc.perform(post("/api/point-transactions/reviews")
				.param("reviewType", "REVIEW")
				.with(user(currentUser))
			)
			.andExpect(status().isCreated());

		verify(pointTransactionService).reviewPointTransaction(any(CurrentUserDetails.class), eq("REVIEW"));
	}

	@Test
	void testReviewPointTransactionWithPhotoReview() throws Exception {
		UserTokenInfo userTokenInfo = UserTokenInfo.builder()
			.id(1L)
			.password("password123")
			.roles(List.of("ROLE_USER", "ROLE_MEMBER"))
			.status("ACTIVE")
			.build();
		CurrentUserDetails currentUser = new CurrentUserDetails(userTokenInfo);

		mockMvc.perform(post("/api/point-transactions/reviews")
				.param("reviewType", "PHOTO_REVIEW")
				.with(user(currentUser))
			)
			.andExpect(status().isCreated());

		verify(pointTransactionService).reviewPointTransaction(any(CurrentUserDetails.class), eq("PHOTO_REVIEW"));
	}

}