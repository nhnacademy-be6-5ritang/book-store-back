package com.nhnacademy.bookstoreback.deliverypolicy.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.bookstoreback.deliverypolicy.domain.dto.request.CreateDeliveryPolicyRequest;
import com.nhnacademy.bookstoreback.deliverypolicy.domain.dto.request.UpdateDeliveryPolicyRequest;
import com.nhnacademy.bookstoreback.deliverypolicy.domain.dto.response.GetDeliveryPoliciesResponse;
import com.nhnacademy.bookstoreback.deliverypolicy.domain.dto.response.GetDeliveryPolicyResponse;
import com.nhnacademy.bookstoreback.deliverypolicy.service.DeliveryPolicyService;

@ExtendWith(MockitoExtension.class)
class DeliveryPolicyControllerTest {

	@Mock
	private DeliveryPolicyService deliveryPolicyService;

	@InjectMocks
	private DeliveryPolicyController deliveryPolicyController;

	private MockMvc mockMvc;
	private ObjectMapper objectMapper;

	private CreateDeliveryPolicyRequest createRequest;
	private UpdateDeliveryPolicyRequest updateRequest;
	private GetDeliveryPoliciesResponse getPoliciesResponse;
	private GetDeliveryPolicyResponse getPolicyResponse;

	@BeforeEach
	void setUp() {
		objectMapper = new ObjectMapper();
		mockMvc = MockMvcBuilders.standaloneSetup(deliveryPolicyController).build();

		createRequest = new CreateDeliveryPolicyRequest("Standard Delivery",
			"Description", new BigDecimal("1500.00"), new BigDecimal("5000.00"));

		updateRequest = new UpdateDeliveryPolicyRequest("Updated Delivery",
			"Updated Description", new BigDecimal("2000.00"), new BigDecimal("6000.00"));

		getPoliciesResponse = new GetDeliveryPoliciesResponse(1L, "Standard Delivery");

		getPolicyResponse = new GetDeliveryPolicyResponse(1L, "Standard Delivery",
			new BigDecimal("1500.00"), "Description", new BigDecimal("5000.00"));
	}

	@Test
	void testGetDeliveryPolicies() throws Exception {
		when(deliveryPolicyService.getDeliveryPolicies()).thenReturn(Collections.singletonList(getPoliciesResponse));

		mockMvc.perform(get("/api/delivery-policies")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().json(objectMapper.writeValueAsString(Collections.singletonList(getPoliciesResponse))));
	}

	@Test
	void testGetDeliveryPolicy() throws Exception {
		when(deliveryPolicyService.getDeliveryPolicy(anyLong())).thenReturn(getPolicyResponse);

		mockMvc.perform(get("/api/delivery-policies/1")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().json(objectMapper.writeValueAsString(getPolicyResponse)));
	}

	@Test
	void testCreateDeliveryPolicy() throws Exception {
		mockMvc.perform(post("/api/delivery-policies")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(createRequest)))
			.andExpect(status().isCreated());
	}

	@Test
	void testUpdateDeliveryPolicy() throws Exception {
		mockMvc.perform(put("/api/delivery-policies/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(updateRequest)))
			.andExpect(status().isOk());
	}

	@Test
	void testDeleteDeliveryPolicy() throws Exception {
		mockMvc.perform(delete("/api/delivery-policies/1")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk());
	}

	@Test
	void testAddPolicy() throws Exception {
		when(deliveryPolicyService.findByDeliveryPolicyStandardPriceLessThanEqualOrderByDeliveryPolicyStandardPriceDesc(
			anyLong(), any(BigDecimal.class)))
			.thenReturn(getPolicyResponse);

		mockMvc.perform(put("/api/delivery-policies/1/1500.00/addPolicies")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().json(objectMapper.writeValueAsString(getPolicyResponse)));
	}
}
