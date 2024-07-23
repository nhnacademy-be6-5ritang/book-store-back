package com.nhnacademy.bookstoreback.delivery;

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
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.bookstoreback.delivery.controller.DeliveryController;
import com.nhnacademy.bookstoreback.delivery.domain.dto.request.UpdateDeliveryByOrderIdRequest;
import com.nhnacademy.bookstoreback.delivery.domain.dto.request.UpdateDeliveryRequest;
import com.nhnacademy.bookstoreback.delivery.domain.dto.response.GetDeliveryResponse;
import com.nhnacademy.bookstoreback.delivery.domain.dto.response.UpdateDeliveryAddOrderPolicyResponse;
import com.nhnacademy.bookstoreback.delivery.domain.dto.response.UpdateDeliveryResponse;
import com.nhnacademy.bookstoreback.delivery.service.DeliveryService;

@WebMvcTest(DeliveryController.class)
class DeliveryControllerTest {

	private MockMvc mockMvc;

	@MockBean
	private DeliveryService deliveryService;

	private ObjectMapper objectMapper;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(new DeliveryController(deliveryService))
			.setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
			.build();
		objectMapper = new ObjectMapper();
	}

	@Test
	@WithMockUser(roles = "MEMBER")
	void getDelivery() throws Exception {
		// GetDeliveryResponse 객체 설정
		GetDeliveryResponse response = GetDeliveryResponse.builder()
			.deliverySenderName("SenderName") // senderName 설정
			.deliverySenderPhone("SenderPhone") // senderPhone 설정
			.deliverySenderAddress("SenderAddress") // senderAddress 설정
			.deliveryReceiver("ReceiverName") // receiver 설정
			.deliveryReceiverPhone("ReceiverPhone") // receiverPhone 설정
			.deliveryReceiverAddress("ReceiverAddress") // receiverAddress 설정
			.orderId(1L) // orderId 설정
			.deliveryStatusName("StatusName") // deliveryStatusName 설정
			.deliveryPolicyStandardPrice(new BigDecimal("100.00")) // deliveryPolicyStandardPrice 설정
			.build();

		// Mocking 서비스 메서드
		when(deliveryService.getDelivery(anyLong()))
			.thenReturn(response);

		// MockMvc 요청 및 검증
		mockMvc.perform(get("/api/deliveries/{deliveryId}", 1L)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().json(objectMapper.writeValueAsString(response)));
	}

	@Test
	@WithMockUser(roles = "MEMBER")
	void updateDelivery() throws Exception {
		// UpdateDeliveryRequest 객체 설정
		Long deliveryStatusId = 2L;  // 예시로 사용하는 ID 값

		// UpdateDeliveryRequest 객체 설정
		UpdateDeliveryRequest request = UpdateDeliveryRequest.builder()
			.deliveryStatusId(deliveryStatusId)
			.build();

		// UpdateDeliveryResponse 객체 설정
		UpdateDeliveryResponse response = UpdateDeliveryResponse.builder()
			.deliveryId(1L) // deliveryId 설정
			.deliveryStatusName("NewStatus") // deliveryStatus 설정
			.build();

		// Mocking 서비스 메서드
		when(deliveryService.updateDelivery(anyLong(), any(UpdateDeliveryRequest.class)))
			.thenReturn(response);

		// MockMvc 요청 및 검증
		mockMvc.perform(put("/api/deliveries/{deliveryId}", 1L)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isOk())
			.andExpect(content().json(objectMapper.writeValueAsString(response)));
	}

	@Test
	@WithMockUser(roles = "MEMBER")
	void addOrder() throws Exception {
		// UpdateDeliveryAddOrderPolicyResponse 객체 설정
		UpdateDeliveryAddOrderPolicyResponse response = UpdateDeliveryAddOrderPolicyResponse.builder()
			.deliveryId(1L) // deliveryId 설정
			.build();

		// Mocking 서비스 메서드
		when(deliveryService.updateDeliveryAddOrder(anyLong(), anyLong()))
			.thenReturn(response);

		// MockMvc 요청 및 검증
		mockMvc.perform(put("/api/deliveries/{deliveryId}/{orderId}/orders", 1L, 1L)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().json(objectMapper.writeValueAsString(response)));
	}

	@Test
	@WithMockUser(roles = "MEMBER")
	void deleteDelivery() throws Exception {
		mockMvc.perform(delete("/api/deliveries/{deliveryId}", 1L)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isNoContent());
	}

	@Test
	@WithMockUser(roles = "MEMBER")
	void getDeliveryByOrder() throws Exception {
		// GetDeliveryResponse 객체 설정
		GetDeliveryResponse response = GetDeliveryResponse.builder()
			.deliverySenderName("SenderName")
			.deliverySenderPhone("SenderPhone")
			.deliverySenderAddress("SenderAddress")
			.deliveryReceiver("ReceiverName")
			.deliveryReceiverPhone("ReceiverPhone")
			.deliveryReceiverAddress("ReceiverAddress")
			.orderId(1L)
			.deliveryStatusName("Status1")
			.deliveryPolicyStandardPrice(BigDecimal.valueOf(100.00))
			.build();

		// Mocking 서비스 메서드
		when(deliveryService.getDeliveryByOrderId(anyLong()))
			.thenReturn(response);

		// MockMvc 요청 및 검증
		mockMvc.perform(get("/api/deliveries/{orderId}/orders", 1L)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().json(objectMapper.writeValueAsString(response)));
	}

	@Test
	@WithMockUser(roles = "MEMBER")
	void updateDeliveryByOrderId() throws Exception {
		// UpdateDeliveryByOrderIdRequest 객체 설정
		UpdateDeliveryByOrderIdRequest request = UpdateDeliveryByOrderIdRequest.builder()
			.senderName("UpdatedSenderName") // senderName 설정
			.senderAddress("UpdatedSenderAddress") // senderAddress 설정
			.senderAddress2("UpdatedSenderAddress2") // senderAddress2 설정
			.senderPhone("UpdatedSenderPhone") // senderPhone 설정
			.build();
		// Mocking 서비스 메서드
		doNothing().when(deliveryService).updateDeliveryByOrderId(anyLong(), any(UpdateDeliveryByOrderIdRequest.class));

		// MockMvc 요청 및 검증
		mockMvc.perform(put("/api/deliveries/sender/{deliveryId}", 1L)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isNoContent());
	}
}
