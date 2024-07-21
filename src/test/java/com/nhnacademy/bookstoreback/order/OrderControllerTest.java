package com.nhnacademy.bookstoreback.order;

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
import com.nhnacademy.bookstoreback.auth.jwt.dto.CurrentUserDetails;
import com.nhnacademy.bookstoreback.book.service.impl.BookServiceImpl;
import com.nhnacademy.bookstoreback.order.controller.OrderController;
import com.nhnacademy.bookstoreback.order.domain.dto.request.CreateBookOrderRequest;
import com.nhnacademy.bookstoreback.order.domain.dto.request.CreateOrderRequest;
import com.nhnacademy.bookstoreback.order.domain.dto.request.CreateOrderStatusRequest;
import com.nhnacademy.bookstoreback.order.domain.dto.request.UpdateRefundPolicyRequest;
import com.nhnacademy.bookstoreback.order.domain.dto.response.CreateBookOrderGetBookResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.CreateBookOrderGetOrderResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.CreateBookOrderResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.CreateOrderResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.GetAdminAllPaperResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.GetAllPaperResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.GetAllRefundResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.GetBookByOrderCouponResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.GetOrderResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.GetOrderStatusResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.GetPaperResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.GetRefundResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.GetUserPointOrderResponse;
import com.nhnacademy.bookstoreback.order.service.impl.BookOrderServiceImpl;
import com.nhnacademy.bookstoreback.order.service.impl.OrderServiceImpl;
import com.nhnacademy.bookstoreback.order.service.impl.OrderStatusServiceImpl;
import com.nhnacademy.bookstoreback.order.service.impl.PaperTypeServiceImpl;
import com.nhnacademy.bookstoreback.order.service.impl.RefundPolicyServiceImpl;
import com.nhnacademy.bookstoreback.order.service.impl.WrappingPaperServiceImpl;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

	private MockMvc mockMvc;

	@MockBean
	private OrderServiceImpl orderServiceImpl;

	@MockBean
	private BookOrderServiceImpl bookOrderServiceImpl;

	@MockBean
	private OrderStatusServiceImpl orderStatusServiceImpl;

	@MockBean
	private WrappingPaperServiceImpl wrappingPaperServiceImpl;

	@MockBean
	private PaperTypeServiceImpl paperTypeServiceImpl;

	@MockBean
	private BookServiceImpl bookServiceImpl;

	@MockBean
	private RefundPolicyServiceImpl refundPolicyServiceImpl;

	private ObjectMapper objectMapper;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(new OrderController(
				orderServiceImpl,
				orderStatusServiceImpl,  // 다른 서비스들은 필요에 따라 추가하거나 Mock으로 설정
				wrappingPaperServiceImpl,
				bookOrderServiceImpl,
				paperTypeServiceImpl,
				bookServiceImpl,
				refundPolicyServiceImpl))
			.build();
		objectMapper = new ObjectMapper();
	}

	@Test
	@WithMockUser(roles = "MEMBER")
	void getOrder() throws Exception {
		// GetOrderResponse 객체 설정
		GetOrderResponse response = GetOrderResponse.builder()
			.infoId("1234")
			.orderPrice(new BigDecimal("100.00"))
			.pointSale(new BigDecimal("10.00"))
			.couponSale(new BigDecimal("5.00"))
			.build();

		// Mocking 서비스 메서드
		when(orderServiceImpl.getOrder(anyLong())).thenReturn(response);

		// 요청 및 응답 검증
		mockMvc.perform(get("/api/orders/{order_id}", 1L)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().json(objectMapper.writeValueAsString(response)));
	}

	@Test
	@WithMockUser(roles = "MEMBER")
	void updateOrderStatus() throws Exception {
		GetOrderResponse response = GetOrderResponse.builder()
			.infoId("1234")
			.orderPrice(new BigDecimal("100.00"))
			.pointSale(new BigDecimal("10.00"))
			.couponSale(new BigDecimal("5.00"))
			.build();

		when(orderServiceImpl.updateOrderStatus(anyLong(), anyLong())).thenReturn(response);

		mockMvc.perform(put("/api/orders/{order_id}/orderStatus/{order_status_id}", 1L, 2L)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().json(objectMapper.writeValueAsString(response)));
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	void createOrderStatus() throws Exception {
		CreateOrderStatusRequest request = new CreateOrderStatusRequest("Processing");
		GetOrderStatusResponse response = new GetOrderStatusResponse(1L, "Processing");

		when(orderStatusServiceImpl.create(any(CreateOrderStatusRequest.class))).thenReturn(response);

		mockMvc.perform(post("/api/orders/orderStatus")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isOk())
			.andExpect(content().json(objectMapper.writeValueAsString(response)));
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	void deleteOrderStatus() throws Exception {
		doNothing().when(orderStatusServiceImpl).delete(anyLong());

		mockMvc.perform(delete("/api/orders/orderStatus/{order_status_id}", 1L))
			.andExpect(status().isNoContent());
	}

	@Test
	@WithMockUser(roles = "MEMBER")
	void getWrappingPaper() throws Exception {
		GetPaperResponse response = new GetPaperResponse(1L, "Gift Wrap", "A beautiful gift wrap",
			new BigDecimal("15.00"));

		when(paperTypeServiceImpl.getPaperTypeById(anyLong())).thenReturn(response);

		mockMvc.perform(get("/api/orders/papers/{paper_id}", 1L)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().json(objectMapper.writeValueAsString(response)));
	}

	@Test
	@WithMockUser(roles = "MEMBER")
	void createBookOrder() throws Exception {
		// CreateBookOrderRequest와 CreateBookOrderResponse 객체 생성
		CreateBookOrderRequest request = new CreateBookOrderRequest(
			1L, // bookId
			1L, // orderId
			2 // quantity
		);

		CreateBookOrderGetBookResponse bookResponse = CreateBookOrderGetBookResponse.builder()
			.build();

		CreateBookOrderGetOrderResponse orderResponse = CreateBookOrderGetOrderResponse.builder()
			.build();

		CreateBookOrderResponse response = CreateBookOrderResponse.builder()
			.getBookResponse(bookResponse)
			.getOrderResponse(orderResponse)
			.quantity(2)
			.orderListId(1L)
			.build();

		// Mocking 서비스 메서드
		when(bookOrderServiceImpl.createBookOrder(any(CreateBookOrderRequest.class))).thenReturn(response);

		// 요청 및 응답 검증
		mockMvc.perform(post("/api/orders/books-orders")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isOk())
			.andExpect(content().json(objectMapper.writeValueAsString(response)));
	}

	@Test
	@WithMockUser(roles = "MEMBER")
	void createOrder() throws Exception {
		// CreateOrderRequest 객체 생성
		CreateOrderRequest request = new CreateOrderRequest(
			"John Doe",                      // payerName
			"john.doe@example.com",          // payerEmail
			"01012345678",                 // payerNumber
			"123 Main St, Anytown, USA",     // payerAddress
			new BigDecimal("100.00"),        // orderPrice
			new BigDecimal("10.00"),         // pointSale
			new BigDecimal("5.00")           // couponSale
		);

		// CreateOrderResponse 객체 생성
		CreateOrderResponse response = CreateOrderResponse.builder()
			.orderId(1L)                     // orderId
			.infoId("ORDER1234")             // infoId
			.orderPrice(new BigDecimal("100.00")) // orderPrice
			.pointSale(new BigDecimal("10.00"))  // pointSale
			.couponSale(new BigDecimal("5.00"))  // couponSale
			.build();

		// Mocking 서비스 메서드
		when(orderServiceImpl.createOrder(any(CreateOrderRequest.class), any(CurrentUserDetails.class)))
			.thenReturn(response);

		// 요청 및 응답 검증
		mockMvc.perform(post("/api/orders/orders")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isOk())
			.andExpect(content().json(objectMapper.writeValueAsString(response)));
	}

	@Test
	@WithMockUser(roles = "MEMBER")
	void getAllWrappingPapers() throws Exception {
		// GetPaperResponse 리스트 생성
		GetPaperResponse paper1 = new GetPaperResponse(1L, "Gift Wrap", "A beautiful gift wrap",
			new BigDecimal("15.00"));
		GetPaperResponse paper2 = new GetPaperResponse(2L, "Holiday Wrap", "Festive holiday wrap",
			new BigDecimal("20.00"));

		// GetAllPaperResponse 객체 생성
		GetAllPaperResponse response = GetAllPaperResponse.builder()
			.papers(List.of(paper1, paper2))
			.build();

		// Mocking 서비스 메서드
		when(paperTypeServiceImpl.getAllPaperTypes()).thenReturn(response);

		// 요청 및 응답 검증
		mockMvc.perform(get("/api/orders/wrappings")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().json(objectMapper.writeValueAsString(response)));
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	void getAdminAllWrappingPapers() throws Exception {
		// GetPaperResponse 객체 생성
		GetPaperResponse paper1 = new GetPaperResponse(1L, "Gift Wrap", "A beautiful gift wrap",
			new BigDecimal("15.00"));
		GetPaperResponse paper2 = new GetPaperResponse(2L, "Holiday Wrap", "Festive holiday wrap",
			new BigDecimal("20.00"));

		// GetAdminAllPaperResponse 객체 생성
		GetAdminAllPaperResponse response = GetAdminAllPaperResponse.builder()
			.papers(List.of(paper1, paper2))
			.build();

		// Mocking 서비스 메서드
		when(paperTypeServiceImpl.getAdminAllPaperTypes()).thenReturn(response);

		// 요청 및 응답 검증
		mockMvc.perform(get("/api/orders/papers/admin")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().json(objectMapper.writeValueAsString(response)));
	}

	@Test
	@WithMockUser(roles = "MEMBER")
	void getBookByOneOrder() throws Exception {
		// GetBookByOrderCouponResponse 객체 생성
		GetBookByOrderCouponResponse response = GetBookByOrderCouponResponse.builder()
			.bookId(1L)
			.bookPrice(new BigDecimal("29.99"))
			.categoryId(List.of(1L, 2L, 3L))
			.build();

		// Mocking 서비스 메서드
		when(bookOrderServiceImpl.getBookAndCategoryByOrderListId(anyLong())).thenReturn(response);

		// 요청 및 응답 검증
		mockMvc.perform(get("/api/orders/{orderListId}/book", 1L)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().json(objectMapper.writeValueAsString(response)));
	}

	@Test
	@WithMockUser(roles = "MEMBER")
	void getUserPointOrders() throws Exception {
		// GetUserPointOrderResponse 객체 생성
		GetUserPointOrderResponse response = GetUserPointOrderResponse.builder()
			.userPoint(new BigDecimal("1234.56"))
			.build();

		// Mocking 서비스 메서드
		when(orderServiceImpl.getUserPoint(any(CurrentUserDetails.class))).thenReturn(response);

		// 요청 및 응답 검증
		mockMvc.perform(get("/api/orders/orders-points")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().json(objectMapper.writeValueAsString(response)));
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	void refundingOrder() throws Exception {
		doNothing().when(orderServiceImpl).refundingOrder(anyString());

		mockMvc.perform(get("/api/orders/refunding/{orderInfoId}", "order123")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isNoContent());
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	void refundedOrder() throws Exception {
		doNothing().when(orderServiceImpl).refundedOrder(anyString());

		mockMvc.perform(get("/api/orders/refunded/{orderInfoId}", "order123")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isNoContent());
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	void getRefundPolicy() throws Exception {
		// GetRefundResponse 객체 생성
		GetRefundResponse refund1 = GetRefundResponse.builder()
			.build();

		GetRefundResponse refund2 = GetRefundResponse.builder()
			.build();

		// GetAllRefundResponse 객체 생성
		GetAllRefundResponse response = GetAllRefundResponse.builder()
			.refunds(List.of(refund1, refund2))
			.build();

		// Mocking 서비스 메서드
		when(refundPolicyServiceImpl.getAllRefundPolicies()).thenReturn(response);

		// 요청 및 응답 검증
		mockMvc.perform(get("/api/orders/refund-policy")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().json(objectMapper.writeValueAsString(response)));
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	void updateRefundPolicy() throws Exception {
		// UpdateRefundPolicyRequest 객체 생성
		UpdateRefundPolicyRequest request = new UpdateRefundPolicyRequest(
			"Updated refund policy content", // 환불 정책 내용
			30 // 환불 정책 날짜
		);

		// Mocking 서비스 메서드
		doNothing().when(refundPolicyServiceImpl).updateRefundPolicy(any(UpdateRefundPolicyRequest.class), anyLong());

		// 요청 및 응답 검증
		mockMvc.perform(put("/api/orders/refund-policy/{refundPolicyId}", 1L)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isNoContent()); // 응답 상태 코드가 204 No Content인지 확인
	}

}
