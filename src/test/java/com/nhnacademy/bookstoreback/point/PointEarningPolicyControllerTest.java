package com.nhnacademy.bookstoreback.point;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.util.List;

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
import com.nhnacademy.bookstoreback.point.earningpolicy.controller.PointEarningPolicyController;
import com.nhnacademy.bookstoreback.point.earningpolicy.domain.dto.request.CreatePointEarningPolicyRequest;
import com.nhnacademy.bookstoreback.point.earningpolicy.domain.dto.request.UpdatePointEarningPolicyRequest;
import com.nhnacademy.bookstoreback.point.earningpolicy.domain.dto.response.CreatePointEarningPolicyResponse;
import com.nhnacademy.bookstoreback.point.earningpolicy.domain.dto.response.GetPointEarningPolicyResponse;
import com.nhnacademy.bookstoreback.point.earningpolicy.domain.dto.response.UpdatePointEarningPolicyResponse;
import com.nhnacademy.bookstoreback.point.earningpolicy.service.PointEarningPolicyService;

@WebMvcTest(PointEarningPolicyController.class)
class PointEarningPolicyControllerTest {

	private MockMvc mockMvc;

	@MockBean
	private PointEarningPolicyService pointEarningPolicyService;

	private ObjectMapper objectMapper;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(new PointEarningPolicyController(pointEarningPolicyService)).build();
		objectMapper = new ObjectMapper();
	}

	@Test
	@WithMockUser(roles = "MEMBER")
	void createPointEarningPolicy() throws Exception {
		// CreatePointEarningPolicyRequest 객체 설정
		CreatePointEarningPolicyRequest request = new CreatePointEarningPolicyRequest(
			"TYPE1", // pointEarningPolicyType
			new BigDecimal("100.00") // pointEarningAmount
		);

		// CreatePointEarningPolicyResponse 객체 설정
		CreatePointEarningPolicyResponse response = new CreatePointEarningPolicyResponse(
			"TYPE1", // pointEarningPolicyType
			new BigDecimal("100.00") // pointEarningAmount
		);

		// mockito 설정
		when(pointEarningPolicyService.createPointEarningPolicy(any(CreatePointEarningPolicyRequest.class)))
			.thenReturn(response);

		// 요청 및 응답 검증
		mockMvc.perform(post("/api/point-earning-policies")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isCreated())
			.andExpect(content().json(objectMapper.writeValueAsString(response)));
	}

	@Test
	@WithMockUser(roles = "MEMBER")
	void getPointEarningPolicies() throws Exception {
		// GetPointEarningPolicyResponse 객체 설정
		GetPointEarningPolicyResponse response = new GetPointEarningPolicyResponse(
			1L, // pointEarningPolicyId
			"TYPE1", // pointEarningPolicyType
			new BigDecimal("100.00"), // pointEarningAmount
			"ACTIVE" // pointEarningPolicyStatus
		);

		List<GetPointEarningPolicyResponse> responseList = List.of(response);

		// Mocking 서비스 메서드
		when(pointEarningPolicyService.getPointEarningPolicies())
			.thenReturn(responseList);

		// MockMvc 요청 및 검증
		mockMvc.perform(get("/api/point-earning-policies")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().json(objectMapper.writeValueAsString(responseList)));
	}

	@Test
	@WithMockUser(roles = "MEMBER")
	void getPointEarningPolicy() throws Exception {
		// GetPointEarningPolicyResponse 객체 설정
		GetPointEarningPolicyResponse response = new GetPointEarningPolicyResponse(
			1L, // pointEarningPolicyId
			"TYPE1", // pointEarningPolicyType
			new BigDecimal("100.00"), // pointEarningAmount
			"ACTIVE" // pointEarningPolicyStatus
		);

		// Mocking 서비스 메서드
		when(pointEarningPolicyService.getPointEarningPolicy(anyLong()))
			.thenReturn(response);

		// MockMvc 요청 및 검증
		mockMvc.perform(get("/api/point-earning-policies/{pointEarningPolicyId}", 1L)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().json(objectMapper.writeValueAsString(response)));
	}

	@Test
	@WithMockUser(roles = "MEMBER")
	void updatePointEarningPolicy() throws Exception {
		// UpdatePointEarningPolicyRequest 객체 설정
		UpdatePointEarningPolicyRequest request = new UpdatePointEarningPolicyRequest(
			"NEW_TYPE", // pointEarningPolicyType
			new BigDecimal("200.00") // pointEarningAmount
		);

		// UpdatePointEarningPolicyResponse 객체 설정
		UpdatePointEarningPolicyResponse response = new UpdatePointEarningPolicyResponse(
			"NEW_TYPE", // pointEarningPolicyType
			new BigDecimal("200.00") // pointEarningAmount
		);

		// Mocking 서비스 메서드
		when(pointEarningPolicyService.updatePointEarningPolicy(anyLong(), any(UpdatePointEarningPolicyRequest.class)))
			.thenReturn(response);

		// MockMvc 요청 및 검증
		mockMvc.perform(patch("/api/point-earning-policies/{pointEarningPolicyId}", 1L)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isOk())
			.andExpect(content().json(objectMapper.writeValueAsString(response)));
	}

	@Test
	@WithMockUser(roles = "MEMBER")
	void activatePointEarningPolicy() throws Exception {
		mockMvc.perform(patch("/api/point-earning-policies/{pointEarningPolicyId}/activate", 1L)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isNoContent());
	}

	@Test
	@WithMockUser(roles = "MEMBER")
	void deactivatePointEarningPolicy() throws Exception {
		mockMvc.perform(patch("/api/point-earning-policies/{pointEarningPolicyId}/deactivate", 1L)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isNoContent());
	}

	@Test
	@WithMockUser(roles = "MEMBER")
	void deletePointEarningPolicy() throws Exception {
		mockMvc.perform(delete("/api/point-earning-policies/{pointEarningPolicyId}", 1L)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isNoContent());
	}
}
