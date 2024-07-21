package com.nhnacademy.bookstoreback.payment;

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

import com.nhnacademy.bookstoreback.auth.jwt.dto.CurrentUserDetails;
import com.nhnacademy.bookstoreback.order.domain.dto.response.FindByInfoIdBookOrderGetBookResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.FindByInfoIdBookOrderGetOrderResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.GetBookOrderByInfoIdResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.GetOrderByInfoResponse;
import com.nhnacademy.bookstoreback.payment.controller.PaymentController;
import com.nhnacademy.bookstoreback.payment.dto.response.CancelResponse;
import com.nhnacademy.bookstoreback.payment.dto.response.PaymentSaveResponse;
import com.nhnacademy.bookstoreback.payment.dto.response.TransactionsResponse;
import com.nhnacademy.bookstoreback.payment.dto.response.UpdatePaymentResponse;
import com.nhnacademy.bookstoreback.payment.service.Impl.PaymentServiceImpl;

@WebMvcTest(PaymentController.class)
class PaymentControllerTest {

	private MockMvc mockMvc;

	@MockBean
	private PaymentServiceImpl paymentServiceImpl;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.initMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(new PaymentController(paymentServiceImpl)).build();
	}

	@Test
	@WithMockUser(roles = "MEMBER")
	void bookOrder() throws Exception {
		// 더미 데이터 생성
		FindByInfoIdBookOrderGetBookResponse bookResponse = FindByInfoIdBookOrderGetBookResponse.builder()
			.build();

		FindByInfoIdBookOrderGetOrderResponse orderResponse = FindByInfoIdBookOrderGetOrderResponse.builder()
			.build();

		GetBookOrderByInfoIdResponse response = GetBookOrderByInfoIdResponse.builder()
			.orderListId(123L)
			.getBookResponse(bookResponse)
			.getOrderResponse(orderResponse)
			.quantity(2)
			.title("Java Programming")
			.build();

		when(paymentServiceImpl.findByCartOrderInfoId(anyString())).thenReturn(response);

		mockMvc.perform(get("/api/payments/books-orders/{order_info_id}", "12345")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().json("{"
				+ "\"orderListId\":123,"
				+ "\"quantity\":2,"
				+ "\"title\":\"Java Programming\""
				+ "}"));
	}

	@Test
	@WithMockUser(roles = "MEMBER")
	void findByOrderInfoId() throws Exception {
		// 더미 데이터 생성
		GetOrderByInfoResponse response = GetOrderByInfoResponse.builder()
			.orderId(101L)
			.infoId("12345")
			.payername("John Doe")
			.payerEmail("john.doe@example.com")
			.payerAddress("123 Main St, Apt 4B")
			.status("Shipped")
			.price(new BigDecimal("299.99"))
			.couponSale(new BigDecimal("20.00"))
			.pointSale(new BigDecimal("10.00"))
			.build();

		when(paymentServiceImpl.findByOrder(anyString())).thenReturn(response);

		mockMvc.perform(get("/api/payments/order-info/{order_info_id}", "12345")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().json("{"
				+ "\"orderId\":101,"
				+ "\"infoId\":\"12345\","
				+ "\"payername\":\"John Doe\","
				+ "\"payerEmail\":\"john.doe@example.com\","
				+ "\"payerAddress\":\"123 Main St, Apt 4B\","
				+ "\"status\":\"Shipped\","
				+ "\"price\":299.99,"
				+ "\"couponSale\":20.00,"
				+ "\"pointSale\":10.00"
				+ "}"));
	}

	@Test
	@WithMockUser(roles = "MEMBER")
	void savePayment() throws Exception {
		PaymentSaveResponse response = PaymentSaveResponse.builder()
			.paymentKey("abcd1234")
			.build();

		when(paymentServiceImpl.savePaymentResponse(anyString(), any(CurrentUserDetails.class))).thenReturn(response);

		mockMvc.perform(post("/api/payments")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"paymentData\": \"someData\"}"))
			.andExpect(status().isOk())
			.andExpect(content().json("{"
				+ "\"paymentKey\":\"abcd1234\""
				+ "}"));
	}

	@Test
	@WithMockUser(roles = "MEMBER")
	void transactions() throws Exception {
		// TransactionsResponse 객체 생성
		TransactionsResponse response = TransactionsResponse.builder()
			.orderId("123456")
			.orderName("Java Programming Book")
			.amount(new BigDecimal("29.99"))
			.status("Completed")
			.provider("PayPal")
			.build();

		// Mock 설정
		when(paymentServiceImpl.transactions(anyString())).thenReturn(response);

		// MockMvc를 사용한 요청 및 응답 검증
		mockMvc.perform(post("/api/payments/transactions")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"transactionData\": \"someData\"}"))
			.andExpect(status().isOk())
			.andExpect(content().json("{"
				+ "\"orderId\":\"123456\","
				+ "\"orderName\":\"Java Programming Book\","
				+ "\"amount\":29.99,"
				+ "\"status\":\"Completed\","
				+ "\"provider\":\"PayPal\""
				+ "}"));
	}

	@Test
	@WithMockUser(roles = "MEMBER")
	void cancelByOrderInfoId() throws Exception {
		// 더미 응답 데이터 생성
		CancelResponse response = CancelResponse.builder()
			.paymentKey("abc123")
			.paymentId(456L)
			.build();

		// Mock 설정
		when(paymentServiceImpl.paymentFindByOrderInfoId(anyString())).thenReturn(response);

		// MockMvc를 사용한 요청 및 응답 검증
		mockMvc.perform(get("/api/payments/cancel/{order_info_id}", "12345")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().json("{"
				+ "\"paymentKey\":\"abc123\","
				+ "\"paymentId\":456"
				+ "}"));
	}

	@Test
	@WithMockUser(roles = "MEMBER")
	void cancelByPaymentId() throws Exception {
		// 더미 응답 데이터 생성
		UpdatePaymentResponse response = UpdatePaymentResponse.builder()
			.status("Success")
			.build();

		// Mock 설정
		when(paymentServiceImpl.updatePayment(anyString(), anyLong())).thenReturn(response);

		// MockMvc를 사용한 요청 및 응답 검증
		mockMvc.perform(post("/api/payments/cancel/test/{payment_id}", 1L)
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"cancelData\": \"someData\"}"))
			.andExpect(status().isOk())
			.andExpect(content().json("{"
				+ "\"status\":\"Success\""
				+ "}"));
	}

}
