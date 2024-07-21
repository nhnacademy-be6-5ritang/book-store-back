package com.nhnacademy.bookstoreback.deliverystatus.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Collections;
import java.util.List;

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
import com.nhnacademy.bookstoreback.deliverystatus.domain.dto.request.CreateDeliveryStatusRequest;
import com.nhnacademy.bookstoreback.deliverystatus.domain.dto.request.UpdateDeliveryStatusRequest;
import com.nhnacademy.bookstoreback.deliverystatus.domain.dto.response.GetDeliveryStatusResponse;
import com.nhnacademy.bookstoreback.deliverystatus.service.DeliveryStatusService;

@WebMvcTest(DeliveryStatusController.class)
class DeliveryStatusControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext webApplicationContext;

	@MockBean
	private DeliveryStatusService deliveryStatusService;

	@Autowired
	private ObjectMapper objectMapper;

	private GetDeliveryStatusResponse getDeliveryStatusResponse;
	private CreateDeliveryStatusRequest createDeliveryStatusRequest;
	private UpdateDeliveryStatusRequest updateDeliveryStatusRequest;

	@BeforeEach
	void setUp() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		getDeliveryStatusResponse = new GetDeliveryStatusResponse(1L, "Shipped");
		createDeliveryStatusRequest = new CreateDeliveryStatusRequest("Shipped");
		updateDeliveryStatusRequest = new UpdateDeliveryStatusRequest("Delivered");
	}

	@Test
	void testGetDeliveryStatuses() throws Exception {
		List<GetDeliveryStatusResponse> responses = Collections.singletonList(getDeliveryStatusResponse);
		when(deliveryStatusService.getDeliveryStatuses()).thenReturn(responses);

		mockMvc.perform(get("/api/deliveryStatuses"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$[0].deliveryStatusId").value(1L))
			.andExpect(jsonPath("$[0].deliveryStatusName").value("Shipped"));
	}

	@Test
	void testGetDeliveryStatus() throws Exception {
		when(deliveryStatusService.getDeliveryStatus(anyLong())).thenReturn(getDeliveryStatusResponse);

		mockMvc.perform(get("/api/deliveryStatuses/1"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.deliveryStatusId").value(1L))
			.andExpect(jsonPath("$.deliveryStatusName").value("Shipped"));
	}

	@Test
	void testCreateDeliveryStatus() throws Exception {
		doNothing().when(deliveryStatusService).createDeliveryStatus(any(CreateDeliveryStatusRequest.class));

		mockMvc.perform(post("/api/deliveryStatuses")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(createDeliveryStatusRequest)))
			.andExpect(status().isCreated());
	}

	@Test
	void testUpdateDeliveryStatus() throws Exception {
		doNothing().when(deliveryStatusService).updateDeliveryStatus(anyLong(), any(UpdateDeliveryStatusRequest.class));

		mockMvc.perform(put("/api/deliveryStatuses/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(updateDeliveryStatusRequest)))
			.andExpect(status().isOk());
	}

	@Test
	void testDeleteDeliveryStatus() throws Exception {
		doNothing().when(deliveryStatusService).deleteDeliveryStatus(anyLong());

		mockMvc.perform(delete("/api/deliveryStatuses/1"))
			.andExpect(status().isOk());
	}
}