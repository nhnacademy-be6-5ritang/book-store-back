package com.nhnacademy.bookstoreback.order;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.bookstoreback.auth.jwt.dto.CurrentUserDetails;
import com.nhnacademy.bookstoreback.book.domain.entity.Book;
import com.nhnacademy.bookstoreback.book.repository.BookRepository;
import com.nhnacademy.bookstoreback.book.service.impl.BookServiceImpl;
import com.nhnacademy.bookstoreback.delivery.domain.entity.Delivery;
import com.nhnacademy.bookstoreback.delivery.repository.DeliveryRepository;
import com.nhnacademy.bookstoreback.deliverystatus.domain.entity.DeliveryStatus;
import com.nhnacademy.bookstoreback.deliverystatus.repository.DeliveryStatusRepository;
import com.nhnacademy.bookstoreback.global.exception.BookOrderFailException;
import com.nhnacademy.bookstoreback.global.exception.OrderFailException;
import com.nhnacademy.bookstoreback.global.exception.OrderStatusFailException;
import com.nhnacademy.bookstoreback.global.exception.PaperFailException;
import com.nhnacademy.bookstoreback.global.exception.payload.ErrorStatus;
import com.nhnacademy.bookstoreback.global.handler.GlobalExceptionHandler;
import com.nhnacademy.bookstoreback.order.controller.OrderController;
import com.nhnacademy.bookstoreback.order.domain.dto.request.CreateBookOrderRequest;
import com.nhnacademy.bookstoreback.order.domain.dto.request.CreateOrderRequest;
import com.nhnacademy.bookstoreback.order.domain.dto.request.CreateOrderStatusRequest;
import com.nhnacademy.bookstoreback.order.domain.dto.request.CreateRefundPolicyRequest;
import com.nhnacademy.bookstoreback.order.domain.dto.request.CreateWrappingTypeRequest;
import com.nhnacademy.bookstoreback.order.domain.dto.request.OrderCheckNonRequest;
import com.nhnacademy.bookstoreback.order.domain.dto.request.UpdateRefundPolicyRequest;
import com.nhnacademy.bookstoreback.order.domain.dto.request.UpdateWrappingTypeRequest;
import com.nhnacademy.bookstoreback.order.domain.dto.response.CreateBookOrderGetBookResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.CreateBookOrderGetOrderResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.CreateBookOrderResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.CreateCartOrderResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.CreateOrderResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.CreatePaperResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.GetAdminAllPaperResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.GetAllListOrderByStatusResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.GetAllListOrderResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.GetAllPaperResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.GetAllRefundResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.GetBookByOrderCouponResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.GetBookOrderGetBookResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.GetBookOrderResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.GetListWrappingResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.GetNonOrderByInfoResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.GetOrderByInfoResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.GetOrderResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.GetOrderStatusResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.GetPaperResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.GetRefundResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.GetUserPointOrderResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.GetWrappingResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.UpdateBookOrderResponse;
import com.nhnacademy.bookstoreback.order.domain.entity.BookOrder;
import com.nhnacademy.bookstoreback.order.domain.entity.Order;
import com.nhnacademy.bookstoreback.order.domain.entity.OrderStatus;
import com.nhnacademy.bookstoreback.order.domain.entity.PaperType;
import com.nhnacademy.bookstoreback.order.domain.entity.RefundPolicy;
import com.nhnacademy.bookstoreback.order.domain.entity.WrappingPaper;
import com.nhnacademy.bookstoreback.order.repository.BookOrderRepository;
import com.nhnacademy.bookstoreback.order.repository.OrderRepository;
import com.nhnacademy.bookstoreback.order.repository.OrderStatusRepository;
import com.nhnacademy.bookstoreback.order.repository.RefundPolicyRepository;
import com.nhnacademy.bookstoreback.order.service.impl.BookOrderServiceImpl;
import com.nhnacademy.bookstoreback.order.service.impl.OrderServiceImpl;
import com.nhnacademy.bookstoreback.order.service.impl.OrderStatusServiceImpl;
import com.nhnacademy.bookstoreback.order.service.impl.PaperTypeServiceImpl;
import com.nhnacademy.bookstoreback.order.service.impl.RefundPolicyServiceImpl;
import com.nhnacademy.bookstoreback.order.service.impl.WrappingPaperServiceImpl;
import com.nhnacademy.bookstoreback.point.transaction.service.impl.PointTransactionServiceImpl;
import com.nhnacademy.bookstoreback.user.domain.dto.response.UserTokenInfo;
import com.nhnacademy.bookstoreback.user.domain.entity.User;
import com.nhnacademy.bookstoreback.user.repository.UserRepository;

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

	@MockBean
	private BookRepository bookRepository;

	@MockBean
	private OrderRepository orderRepository;

	@MockBean
	private BookOrderRepository bookOrderRepository;

	@MockBean
	private OrderStatusRepository orderStatusRepository;

	@MockBean
	private PointTransactionServiceImpl pointTransactionService;

	@MockBean
	private DeliveryStatusRepository deliveryStatusRepository;

	@MockBean
	private DeliveryRepository deliveryRepository;

	@MockBean
	private RefundPolicyRepository refundPolicyRepository;

	@MockBean
	private UserRepository userRepository;

	private ObjectMapper objectMapper;

	public static final String ERROR_PAPER_EXITS = "포장지를 가져올 수 없습니다";

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
			.setControllerAdvice(new GlobalExceptionHandler())
			.build();
		objectMapper = new ObjectMapper();
	}

	@Test
	void getOrder_OrderExists() throws Exception {
		// Arrange: 주문이 존재할 때 반환할 응답 객체 설정
		GetOrderResponse response = GetOrderResponse.builder()
			.infoId("ORD12345")
			.orderPrice(new BigDecimal("150.00"))
			.pointSale(new BigDecimal("10.00"))
			.couponSale(new BigDecimal("5.00"))
			.build();

		when(orderServiceImpl.getOrder(anyLong())).thenReturn(response);

		// Act & Assert: GET 요청을 수행하고 응답 검증
		mockMvc.perform(get("/api/orders/{order_id}", 1L)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.infoId").value("ORD12345"))
			.andExpect(jsonPath("$.orderPrice").value(150.00))
			.andExpect(jsonPath("$.pointSale").value(10.00))
			.andExpect(jsonPath("$.couponSale").value(5.00));
	}

	@Test
	void getOrder_OrderNotFound() throws Exception {
		// Arrange: 주문이 존재하지 않을 때 예외 발생
		when(orderServiceImpl.getOrder(anyLong())).thenThrow(
			new OrderFailException(
				ErrorStatus.from("Order not found", HttpStatus.NOT_FOUND, LocalDateTime.now())
			)
		);

		// Act & Assert: GET 요청을 수행하고 예외 응답 검증
		mockMvc.perform(get("/api/orders/{order_id}", 1L)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.message").value("Order not found"))
			.andExpect(jsonPath("$.timestamp").exists());
	}

	@Test
	void updateOrderStatus_OrderNotFound() throws Exception {
		// Arrange: 주문이 존재하지 않는 경우
		when(orderServiceImpl.updateOrderStatus(anyLong(), anyLong())).thenThrow(
			new OrderFailException(ErrorStatus.from("Order not found", HttpStatus.NOT_FOUND, LocalDateTime.now()))
		);

		// Act & Assert: PUT 요청을 수행하고 예외 응답 검증
		mockMvc.perform(put("/api/orders/{order_id}/orderStatus/{order_status_id}", 1L, 2L)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.message").value("Order not found"))
			.andExpect(jsonPath("$.timestamp").exists());
	}

	@Test
	void updateOrderStatus_StatusNotFound() throws Exception {
		// Arrange: 주문 상태가 존재하지 않는 경우
		when(orderServiceImpl.updateOrderStatus(anyLong(), anyLong())).thenThrow(
			new OrderFailException(
				ErrorStatus.from("Order status not found", HttpStatus.NOT_FOUND, LocalDateTime.now()))
		);

		// Act & Assert: PUT 요청을 수행하고 예외 응답 검증
		mockMvc.perform(put("/api/orders/{order_id}/orderStatus/{order_status_id}", 1L, 2L)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.message").value("Order status not found"))
			.andExpect(jsonPath("$.timestamp").exists());
	}

	@Test
	void updateOrderStatus_Success() throws Exception {
		// Arrange: 주문 및 주문 상태가 존재하는 경우
		Order order = Order.builder()
			.orderPrice(new BigDecimal("100.00"))
			.orderPointSale(new BigDecimal("10.00"))
			.orderCouponSale(new BigDecimal("5.00"))
			.build();
		OrderStatus orderStatus = OrderStatus.builder().build();
		GetOrderResponse response = GetOrderResponse.from(order);

		when(orderServiceImpl.updateOrderStatus(anyLong(), anyLong())).thenReturn(response);

		// Act & Assert: PUT 요청을 수행하고 성공적인 응답 검증
		mockMvc.perform(put("/api/orders/{order_id}/orderStatus/{order_status_id}", 1L, 1L)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.infoId").value(order.getOrderInfoId()))
			.andExpect(jsonPath("$.orderPrice").value(order.getOrderPrice().doubleValue()))
			.andExpect(jsonPath("$.pointSale").value(order.getOrderPointSale().doubleValue()))
			.andExpect(jsonPath("$.couponSale").value(order.getOrderCouponSale().doubleValue()));
	}

	@Test
	void createOrderStatus_Success() throws Exception {
		// Arrange: 유효한 요청 및 응답 설정
		CreateOrderStatusRequest createOrderStatusRequest = new CreateOrderStatusRequest("NEW_STATUS");
		OrderStatus orderStatus = OrderStatus.builder()
			.orderStatusName("NEW_STATUS")
			.build();
		GetOrderStatusResponse response = GetOrderStatusResponse.from(orderStatus);

		when(orderStatusServiceImpl.create(any(CreateOrderStatusRequest.class))).thenReturn(response);

		// Act & Assert: POST 요청을 수행하고 성공적인 응답 검증
		mockMvc.perform(post("/api/orders/orderStatus")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(createOrderStatusRequest)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.orderStatusName").value("NEW_STATUS"));
	}

	// @Test
	// void createOrderStatus_InvalidRequest() throws Exception {
	// 	// Arrange: 유효하지 않은 요청 설정
	// 	CreateOrderStatusRequest invalidRequest = new CreateOrderStatusRequest(""); // 빈 상태 값
	//
	// 	// Act & Assert: POST 요청을 수행하고 유효성 검사 실패 응답 검증
	// 	mockMvc.perform(post("/api/orders/orderStatus")
	// 			.contentType(MediaType.APPLICATION_JSON)
	// 			.content(objectMapper.writeValueAsString(invalidRequest)))
	// 		.andExpect(status().isBadRequest());
	// }

	@Test
	void updateOrderByStatus_Success() throws Exception {
		// Arrange
		CreateOrderStatusRequest request = new CreateOrderStatusRequest("Updated1");
		OrderStatus updatedOrderStatus = new OrderStatus("Updated2");
		GetOrderStatusResponse response = GetOrderStatusResponse.from(updatedOrderStatus);

		when(orderStatusServiceImpl.update(request, 1L)).thenReturn(response);

		// Act & Assert
		mockMvc.perform(put("/api/orders/orderStatus/{order_status_id}", 1L)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.orderStatusName").value("Updated2"));
	}

	@Test
	void updateOrderStatus_NotFound() throws Exception {
		// Arrange
		Long orderStatusId = 1L;
		CreateOrderStatusRequest request = new CreateOrderStatusRequest("Updated1");
		ErrorStatus errorStatus = ErrorStatus.from("Order status not found", HttpStatus.NOT_FOUND, LocalDateTime.now());

		when(orderStatusServiceImpl.update(request, orderStatusId))
			.thenThrow(new OrderStatusFailException(errorStatus));

		// Act & Assert
		mockMvc.perform(put("/api/orders/orderStatus/{order_status_id}", orderStatusId)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.message").value("Order status not found"))
			.andExpect(jsonPath("timestamp").exists());
	}

	// @Test
	// void updateOrderStatus_InvalidRequest() throws Exception {
	// 	// Arrange
	// 	Long orderStatusId = 1L;
	// 	CreateOrderStatusRequest invalidRequest = new CreateOrderStatusRequest(""); // 빈 상태 값
	//
	// 	// Act & Assert
	// 	mockMvc.perform(put("/api/orders/orderStatus/{order_status_id}", orderStatusId)
	// 			.contentType(MediaType.APPLICATION_JSON)
	// 			.content(objectMapper.writeValueAsString(invalidRequest)))
	// 		.andExpect(status().isBadRequest());
	// }

	@Test
	void deleteOrderStatus_Success() throws Exception {
		// Arrange
		Long orderStatusId = 1L;

		// Act & Assert
		mockMvc.perform(delete("/api/orders/orderStatus/{order_status_id}", orderStatusId))
			.andExpect(status().isNoContent());

		// Verify that the delete method was called with the correct ID
		verify(orderStatusServiceImpl).delete(orderStatusId);
	}

	@Test
	void deleteOrderStatus_NotFound() throws Exception {
		// Arrange
		Long orderStatusId = 1L;
		ErrorStatus errorStatus = ErrorStatus.from("Order status not found", HttpStatus.NOT_FOUND, LocalDateTime.now());

		doThrow(new OrderStatusFailException(errorStatus))
			.when(orderStatusServiceImpl).delete(orderStatusId);

		// Act & Assert
		mockMvc.perform(delete("/api/orders/orderStatus/{order_status_id}", orderStatusId))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.message").value("Order status not found"))
			.andExpect(jsonPath("timestamp").exists());
	}

	@Test
	void deleteOrderStatus_Exception() throws Exception {
		// Arrange
		Long orderStatusId = 1L;
		ErrorStatus errorStatus = ErrorStatus.from("Order status not found", HttpStatus.INTERNAL_SERVER_ERROR,
			LocalDateTime.now());
		doThrow(new OrderStatusFailException(errorStatus))
			.when(orderStatusServiceImpl).delete(orderStatusId);

		// Act & Assert
		mockMvc.perform(delete("/api/orders/orderStatus/{order_status_id}", orderStatusId))
			.andExpect(status().isInternalServerError())
			.andExpect(jsonPath("$.message").value("Order status not found"))
			.andExpect(jsonPath("timestamp").exists());

		// Verify that the delete method was called with the correct ID
		verify(orderStatusServiceImpl).delete(orderStatusId);
	}

	@Test
	void orderStatusAll() throws Exception {
		// Arrange: 서비스 메서드 호출 시 반환될 주문 상태 목록 설정
		GetOrderStatusResponse status1 = GetOrderStatusResponse.builder()
			.orderStatusId(1L)
			.orderStatusName("Status 1")
			.build();
		GetOrderStatusResponse status2 = GetOrderStatusResponse.builder()
			.orderStatusId(2L)
			.orderStatusName("Status 2")
			.build();
		List<GetOrderStatusResponse> statuses = Arrays.asList(status1, status2);

		when(orderStatusServiceImpl.findAll()).thenReturn(statuses);

		// Act & Assert: GET 요청을 수행하고 응답 검증
		mockMvc.perform(get("/api/orders/orderStatus/all")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$[0].orderStatusName").value("Status 1"))
			.andExpect(jsonPath("$[1].orderStatusName").value("Status 2"));
	}

	@Test
	void getOrderStatus_Success() throws Exception {
		// Arrange: 서비스 메서드 호출 시 반환될 주문 상태 설정
		Long orderStatusId = 1L;
		GetOrderStatusResponse response = GetOrderStatusResponse.builder()
			.orderStatusId(orderStatusId)
			.orderStatusName("Status 1")
			.build();

		when(orderStatusServiceImpl.findById(orderStatusId)).thenReturn(response);

		// Act & Assert: GET 요청을 수행하고 응답 검증
		mockMvc.perform(get("/api/orders/orderStatus/{order_status_id}", orderStatusId)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.orderStatusName").value("Status 1"));
	}

	@Test
	void getOrderStatus_NotFound() throws Exception {
		// Arrange: 존재하지 않는 주문 상태 아이디로 예외 발생
		Long orderStatusId = 1L;
		ErrorStatus errorStatus = ErrorStatus.from(
			"Order status not found",
			HttpStatus.NOT_FOUND,
			LocalDateTime.now()
		);

		when(orderStatusServiceImpl.findById(orderStatusId)).thenThrow(
			new OrderStatusFailException(errorStatus)
		);

		// Act & Assert: GET 요청을 수행하고 예외 응답 검증
		mockMvc.perform(get("/api/orders/orderStatus/{order_status_id}", orderStatusId)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.message").value("Order status not found"))
			.andExpect(jsonPath("timestamp").exists());
	}

	@Test
	void getWrappingPaper_Success() throws Exception {
		// Arrange: 서비스 메서드 호출 시 반환될 포장지 정보 설정
		Long paperId = 1L;
		GetPaperResponse response = GetPaperResponse.builder()
			.paperId(paperId)
			.paperName("Wrapping Paper 1")
			.paperContent("Content 1")
			.paperPrice(BigDecimal.ONE)
			.build();

		when(paperTypeServiceImpl.getPaperTypeById(paperId)).thenReturn(response);

		// Act & Assert: GET 요청을 수행하고 응답 검증
		mockMvc.perform(get("/api/orders/papers/{paper_id}", paperId)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().json(objectMapper.writeValueAsString(response)));
	}

	@Test
	void getWrappingPaper_NotFound() throws Exception {
		// Arrange: 존재하지 않는 포장지 아이디로 예외 발생
		Long paperId = 1L;
		ErrorStatus errorStatus = ErrorStatus.from(
			"Wrapping paper not found",
			HttpStatus.NOT_FOUND,
			LocalDateTime.now()
		);

		when(paperTypeServiceImpl.getPaperTypeById(paperId)).thenThrow(
			new PaperFailException(errorStatus)
		);

		// Act & Assert: GET 요청을 수행하고 예외 응답 검증
		mockMvc.perform(get("/api/orders/papers/{paper_id}", paperId)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.message").value("Wrapping paper not found"))
			.andExpect(jsonPath("timestamp").exists());
	}

	@Test
	void deleteWrappingPaper_Success() throws Exception {
		// Arrange: 포장지 삭제가 정상적으로 이루어지는 경우
		Long paperTypeId = 1L;
		doNothing().when(wrappingPaperServiceImpl).deleteWrappingPapers(paperTypeId);

		// Act & Assert: DELETE 요청을 수행하고 성공 응답 검증
		mockMvc.perform(delete("/api/orders/wrapping/{paper_type_id}", paperTypeId))
			.andExpect(status().isNoContent()); // HTTP 204 No Content
	}

	@Test
	void deleteWrappingPaper_NotFound() throws Exception {
		// Arrange: 존재하지 않는 포장지 아이디로 삭제 시 예외 발생
		Long paperTypeId = 1L;
		ErrorStatus errorStatus = ErrorStatus.from(
			"Wrapping paper not found",
			HttpStatus.NOT_FOUND,
			LocalDateTime.now()
		);

		doThrow(new PaperFailException(errorStatus)).when(wrappingPaperServiceImpl).deleteWrappingPapers(paperTypeId);

		// Act & Assert: DELETE 요청을 수행하고 예외 응답 검증
		mockMvc.perform(delete("/api/orders/wrapping/{paper_type_id}", paperTypeId))
			.andExpect(status().isNotFound()) // HTTP 404 Not Found
			.andExpect(jsonPath("$.message").value("Wrapping paper not found"))
			.andExpect(jsonPath("timestamp").exists());
	}

	@Test
	void createPaper_Success() throws Exception {
		// Arrange: 정상적인 포장지 생성 요청
		CreateWrappingTypeRequest request = new CreateWrappingTypeRequest("Paper Name", "Paper Content",
			new BigDecimal(100));
		PaperType paperType = PaperType.toEntity(request);
		CreatePaperResponse response = CreatePaperResponse.from(paperType);

		when(paperTypeServiceImpl.createPaper(any(CreateWrappingTypeRequest.class))).thenReturn(response);

		// Act & Assert: POST 요청을 수행하고 성공 응답 검증
		mockMvc.perform(post("/api/orders/papers")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.paperName").value("Paper Name"))
			.andExpect(jsonPath("$.paperContent").value("Paper Content"))
			.andExpect(jsonPath("$.paperPrice").value(100));
	}

	// @Test
	// void createPaper_InvalidRequest() throws Exception {
	// 	// Arrange: 유효하지 않은 요청 (필수 필드가 비어 있는 경우)
	// 	CreateWrappingTypeRequest invalidRequest = new CreateWrappingTypeRequest("", "", BigDecimal.ZERO);
	//
	// 	// Act & Assert: POST 요청을 수행하고 유효성 검사 실패 응답 검증
	// 	mockMvc.perform(post("/api/orders/papers")
	// 			.contentType(MediaType.APPLICATION_JSON)
	// 			.content(objectMapper.writeValueAsString(invalidRequest)))
	// 		.andExpect(status().isBadRequest());
	// }

	@Test
	void updatePaper_Success() throws Exception {
		// Arrange: 정상적인 포장지 업데이트 요청
		UpdateWrappingTypeRequest request = new UpdateWrappingTypeRequest("Updated Paper Name", "Updated Paper Content",
			new BigDecimal(200));
		PaperType paperType = PaperType.builder()
			.paperName("Updated Paper Name")
			.paperContent("Updated Paper Content")
			.paperPrice(new BigDecimal(200))
			.build();

		GetPaperResponse response = GetPaperResponse.from(paperType);

		when(paperTypeServiceImpl.updatePaperTypeById(anyLong(), any(UpdateWrappingTypeRequest.class))).thenReturn(
			response);

		// Act & Assert: PUT 요청을 수행하고 성공 응답 검증
		mockMvc.perform(put("/api/orders/papers/{paper_type_id}", 1L)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.paperName").value("Updated Paper Name"))
			.andExpect(jsonPath("$.paperContent").value("Updated Paper Content"))
			.andExpect(jsonPath("$.paperPrice").value(200));
	}

	@Test
	void updatePaper_NotFound() throws Exception {
		// Arrange: 포장지 종류가 존재하지 않을 때
		UpdateWrappingTypeRequest request = new UpdateWrappingTypeRequest("Updated Paper Name", "Updated Paper Content",
			new BigDecimal(200));

		when(paperTypeServiceImpl.updatePaperTypeById(anyLong(), any(UpdateWrappingTypeRequest.class)))
			.thenThrow(
				new PaperFailException(ErrorStatus.from("포장지가 존재하지 않습니다", HttpStatus.NOT_FOUND, LocalDateTime.now())));

		// Act & Assert: PUT 요청을 수행하고 404 응답 검증
		mockMvc.perform(put("/api/orders/papers/{paper_type_id}", 1L)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.message").value("포장지가 존재하지 않습니다"));
	}

	@Test
	void deletePaper_Success() throws Exception {
		// Arrange: 정상적인 삭제 요청을 준비
		long paperTypeId = 1L;
		doNothing().when(paperTypeServiceImpl).deletePaperTypeById(paperTypeId);

		// Act & Assert: DELETE 요청을 수행하고 성공 응답 검증
		mockMvc.perform(delete("/api/orders/papers/{paper_type_id}", paperTypeId))
			.andExpect(status().isNoContent()); // HTTP 204 No Content
	}

	@Test
	void deletePaper_NotFound() throws Exception {
		// Arrange: 삭제하려는 포장지가 존재하지 않을 때 예외를 발생시킵니다
		long paperTypeId = 1L;

		ErrorStatus errorStatus = ErrorStatus.from(
			"Wrapping paper not found",
			HttpStatus.NOT_FOUND,
			LocalDateTime.now()
		);

		doThrow(new PaperFailException(errorStatus)).when(paperTypeServiceImpl)
			.deletePaperTypeById(paperTypeId);

		// Act & Assert: DELETE 요청을 수행하고 404 Not Found 응답 검증
		mockMvc.perform(delete("/api/orders/papers/{paper_type_id}", paperTypeId))
			.andExpect(status().isNotFound()) // HTTP 404 Not Found
			.andExpect(jsonPath("$.message").value("Wrapping paper not found"))
			.andExpect(jsonPath("timestamp").exists());
	}

	@Test
	void getBookOrder_Success() throws Exception {
		long orderListId = 1L;

		Book mockBook = mock(Book.class);

		Order mockOrder = mock(Order.class);

		// Mock BookOrder 객체 생성
		BookOrder bookOrder = BookOrder.builder()
			.bookQuantity(2)
			.book(mockBook)
			.order(mockOrder)
			.build();

		GetBookOrderResponse response = GetBookOrderResponse.from(
			GetBookOrderGetBookResponse.from(bookOrder.getBook()),
			bookOrder.getBookQuantity(),
			bookOrder.getOrderListId()
		);

		when(bookOrderServiceImpl.getBookOrder(orderListId)).thenReturn(response);

		mockMvc.perform(get("/api/orders/books-orders/{order_list_id}", orderListId))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.quantity").value(2));
	}

	@Test
	void getBookOrder_NotFound() throws Exception {
		// Arrange: 존재하지 않는 주문 리스트 요청
		long orderListId = 1L;
		when(bookOrderServiceImpl.getBookOrder(orderListId))
			.thenThrow(new BookOrderFailException(ErrorStatus.from(
				"주문 리스트를 찾을 수 없습니다",
				HttpStatus.UNPROCESSABLE_ENTITY,
				LocalDateTime.now()
			)));

		// Act & Assert: GET 요청을 수행하고 예외 응답 검증
		mockMvc.perform(get("/api/orders/books-orders/{order_list_id}", orderListId))
			.andExpect(status().isUnprocessableEntity())
			.andExpect(jsonPath("$.message").value("주문 리스트를 찾을 수 없습니다"));
	}

	@Test
	void getCartOrder_Success() throws Exception {
		// Arrange: 정상적인 장바구니 주문 요청
		String orderInfoId = "12345";

		// Mock BookOrder 객체 생성
		BookOrder mockBookOrder = BookOrder.builder()
			.bookQuantity(2)
			.book(mock(Book.class))
			.order(mock(Order.class))
			.build();

		// GetBookOrderResponse 객체 생성
		GetBookOrderResponse mockResponse = GetBookOrderResponse.from(
			GetBookOrderGetBookResponse.from(mockBookOrder.getBook()),
			mockBookOrder.getBookQuantity(),
			mockBookOrder.getOrderListId(),
			mockBookOrder.getOrder().getOrderId()
		);

		List<GetBookOrderResponse> responseList = Collections.singletonList(mockResponse);

		// Mock Service 설정
		when(bookOrderServiceImpl.getBookOrderByOrderId(orderInfoId)).thenReturn(responseList);

		// Act & Assert: GET 요청을 수행하고 성공 응답 검증
		mockMvc.perform(get("/api/orders/book-orders/cart-order/{orderInfoId}", orderInfoId))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$[0].quantity").value(2));
	}

	@Test
	void createBookOrder_Success() throws Exception {
		CreateBookOrderRequest request = new CreateBookOrderRequest(1L, 2L, 2);
		Book mockBook = mock(Book.class); // 실제 Book 엔티티를 생성하거나 Mock 객체를 사용
		Order mockOrder = mock(Order.class); // 실제 Order 엔티티를 생성하거나 Mock 객체를 사용

		BookOrder mockBookOrder = BookOrder.toEntity(2, mockBook, mockOrder);

		CreateBookOrderResponse mockResponse = CreateBookOrderResponse.from(
			CreateBookOrderGetBookResponse.from(mockBook),
			CreateBookOrderGetOrderResponse.from(mockOrder),
			2,
			1L
		);

		when(bookRepository.findById(1L)).thenReturn(Optional.of(mockBook));
		when(bookOrderRepository.save(any(BookOrder.class))).thenReturn(mockBookOrder);
		when(bookOrderServiceImpl.createBookOrder(any(CreateBookOrderRequest.class))).thenReturn(mockResponse);

		// Act & Assert: POST 요청을 수행하고 성공 응답 검증
		mockMvc.perform(post("/api/orders/books-orders")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.quantity").value(2))
			.andExpect(jsonPath("$.orderListId").value(1L));
	}

	@Test
	void getAllWrappingPapers_Success() throws Exception {
		// Arrange: 포장지 종류 리스트 생성
		PaperType paperType1 = PaperType.builder()
			.paperName("Wrapping Paper 1")
			.paperContent("Description 1")
			.paperPrice(new BigDecimal("100"))
			.build();

		PaperType paperType2 = PaperType.builder()
			.paperName("Wrapping Paper 2")
			.paperContent("Description 2")
			.paperPrice(new BigDecimal("200"))
			.build();

		List<PaperType> allPapers = Arrays.asList(paperType1, paperType2);

		GetAllPaperResponse expectedResponse = GetAllPaperResponse.from(allPapers);

		when(paperTypeServiceImpl.getAllPaperTypes()).thenReturn(expectedResponse);

		// Act & Assert: GET 요청을 수행하고 성공 응답 검증
		mockMvc.perform(get("/api/orders/wrappings"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.papers[0].paperName").value("Wrapping Paper 1"))
			.andExpect(jsonPath("$.papers[0].paperPrice").value(100))
			.andExpect(jsonPath("$.papers[1].paperName").value("Wrapping Paper 2"))
			.andExpect(jsonPath("$.papers[1].paperPrice").value(200));
	}

	@Test
	void getAdminAllWrappingPapers_Success() throws Exception {
		// Arrange: 샘플 데이터 준비
		PaperType paperType1 = new PaperType("Paper Name 1", "Paper Content 1", new BigDecimal("100.00"));
		PaperType paperType2 = new PaperType("Paper Name 2", "Paper Content 2", new BigDecimal("150.00"));
		GetPaperResponse paperResponse1 = GetPaperResponse.from(paperType1);
		GetPaperResponse paperResponse2 = GetPaperResponse.from(paperType2);
		GetAdminAllPaperResponse getAdminAllPaperResponse = GetAdminAllPaperResponse.builder()
			.papers(List.of(paperResponse1, paperResponse2))
			.build();

		when(paperTypeServiceImpl.getAdminAllPaperTypes()).thenReturn(getAdminAllPaperResponse);

		// Act & Assert: GET 요청을 수행하고 성공 응답 검증
		mockMvc.perform(get("/api/orders/papers/admin")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().json(objectMapper.writeValueAsString(getAdminAllPaperResponse)));
	}

	@Test
	void createWrappingPapers_Success() throws Exception {
		// Arrange: 샘플 데이터 준비
		Long paperId = 1L;
		Long bookOrderId = 2L;
		Integer quantity = 10;

		PaperType paperType = new PaperType("Gift Wrap", "Colorful wrapping paper", new BigDecimal("5.00"));

		BookOrder bookOrder = mock(BookOrder.class);

		WrappingPaper wrappingPaper = WrappingPaper.toEntity(bookOrder, paperType, quantity);

		GetWrappingResponse getWrappingResponse = GetWrappingResponse.from(wrappingPaper);

		when(wrappingPaperServiceImpl.createWrappingPapers(paperId, bookOrderId, quantity))
			.thenReturn(getWrappingResponse);

		// Act & Assert: POST 요청을 수행하고 성공 응답 검증
		mockMvc.perform(
				post("/api/orders/wrappings/{paper_id}/{book_order_id}/{quantity}", paperId, bookOrderId, quantity)
					.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().json(objectMapper.writeValueAsString(getWrappingResponse)));
	}

	@Test
	void getWrappingPaperByOrderListId_Success() throws Exception {
		// Arrange: 샘플 데이터 준비
		Long orderListId = 1L;

		PaperType paperType1 = new PaperType("Gift Wrap", "Colorful wrapping paper", new BigDecimal("5.00"));

		PaperType paperType2 = new PaperType("Simple Wrap", "Plain wrapping paper", new BigDecimal("3.00"));

		WrappingPaper wrappingPaper1 = WrappingPaper.toEntity(null, paperType1, 5);

		WrappingPaper wrappingPaper2 = WrappingPaper.toEntity(null, paperType2, 10);

		List<WrappingPaper> wrappingPapers = Arrays.asList(wrappingPaper1, wrappingPaper2);

		GetListWrappingResponse getListWrappingResponse = GetListWrappingResponse.from(wrappingPapers);

		when(wrappingPaperServiceImpl.getWrappingPaperByOrderListId(orderListId))
			.thenReturn(getListWrappingResponse);

		// Act & Assert: GET 요청을 수행하고 성공 응답 검증
		mockMvc.perform(get("/api/orders/books-orders/{order_list_id}/wrapping-papers", orderListId)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().json(objectMapper.writeValueAsString(getListWrappingResponse)));
	}

	@Test
	@WithMockUser
	void createOrder_Success() throws Exception {
		// Arrange: 샘플 데이터 준비
		CreateOrderRequest createOrderRequest = CreateOrderRequest.builder()
			.payerName("John Doe")
			.payerEmail("john.doe@example.com")
			.payerNumber("01012345678")
			.payerAddress("123 Main St, Anytown")
			.orderPrice(new BigDecimal("100.00"))
			.pointSale(new BigDecimal("10.00"))
			.couponSale(new BigDecimal("5.00"))
			.build();

		OrderStatus orderStatus = mock(OrderStatus.class);

		Order order = Order.builder()
			.orderInfoId("INFO123")
			.orderPrice(new BigDecimal("100.00"))
			.orderPointSale(new BigDecimal("10.00"))
			.orderCouponSale(new BigDecimal("5.00"))
			.build();

		CreateOrderResponse createOrderResponse = CreateOrderResponse.from(order);

		when(orderServiceImpl.createOrder(any(CreateOrderRequest.class), any(CurrentUserDetails.class)))
			.thenReturn(createOrderResponse);

		// Act & Assert: POST 요청을 수행하고 성공 응답 검증
		mockMvc.perform(post("/api/orders/orders")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(createOrderRequest)))
			.andExpect(status().isOk())
			.andExpect(content().json(objectMapper.writeValueAsString(createOrderResponse)));
	}

	@Test
	void updateBookOrder_Success() throws Exception {
		// Arrange: 샘플 데이터 준비
		Long bookListId = 1L;
		Long orderId = 2L;

		Book book = Book.builder()
			.bookTitle("Sample Book Title")
			.bookPrice(new BigDecimal("50.00"))
			.build();

		Order order = mock(Order.class);

		BookOrder bookOrder = BookOrder.builder()
			.book(book)
			.order(order)
			.bookQuantity(2).build();

		UpdateBookOrderResponse updateBookOrderResponse = UpdateBookOrderResponse.from(bookOrder);

		when(bookOrderServiceImpl.updateOrder(bookListId, orderId))
			.thenReturn(updateBookOrderResponse);

		// Act & Assert: PUT 요청을 수행하고 성공 응답 검증
		mockMvc.perform(put("/api/orders/books-orders/{book_list_id}/{order_id}", bookListId, orderId)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().json(objectMapper.writeValueAsString(updateBookOrderResponse)));
	}

	@Test
	void findAllByUserId_Success() throws Exception {
		CurrentUserDetails currentUserDetails = new CurrentUserDetails(
			new UserTokenInfo(1L, "password", Collections.emptyList(), "ACTIVE"));

		Order order1 = mock(Order.class);

		Order order2 = mock(Order.class);

		List<Order> orders = Arrays.asList(order1, order2);
		GetAllListOrderResponse response = GetAllListOrderResponse.from(orders);

		when(orderServiceImpl.findAllUserId(currentUserDetails)).thenReturn(response);

		// Act & Assert: GET 요청을 수행하고 성공 응답 검증
		mockMvc.perform(get("/api/orders/users/all")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk());

	}

	@Test
	void findByOrderInfoId_Success() throws Exception {
		// Arrange: 샘플 주문 데이터 준비
		String orderInfoId = "ORDER123";
		Order order = mock(Order.class);

		// Mock 설정
		when(order.getOrderId()).thenReturn(1L);
		when(order.getOrderInfoId()).thenReturn(orderInfoId);
		when(order.getOrderPrice()).thenReturn(new BigDecimal("100.00"));
		when(order.getOrderStatus()).thenReturn(new OrderStatus("test"));

		GetOrderByInfoResponse response = GetOrderByInfoResponse.from(order);
		when(orderServiceImpl.findByOrderInfoId(orderInfoId)).thenReturn(response);

		// Act & Assert: GET 요청을 수행하고 성공 응답 검증
		mockMvc.perform(get("/api/orders/order-info/{order_info_id}", orderInfoId)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().json(objectMapper.writeValueAsString(response)));
	}

	@Test
	void findByOrderInfoId_NotFound() throws Exception {
		// Arrange: 존재하지 않는 주문 정보 ID
		String orderInfoId = "ORDER999";
		when(orderServiceImpl.findByOrderInfoId(orderInfoId)).thenThrow(new OrderFailException(
			ErrorStatus.from("ORDER_NOT_FOUND", HttpStatus.NOT_FOUND, LocalDateTime.now())
		));

		// Act & Assert: GET 요청을 수행하고 404 응답 검증
		mockMvc.perform(get("/api/orders/order-info/{order_info_id}", orderInfoId)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isNotFound());
	}

	@Test
	void getOrderStatusWait_Success() throws Exception {
		// Arrange
		Long statusId = 1L;
		OrderStatus orderStatus = new OrderStatus("대기");

		Order order1 = mock(Order.class);

		Order order2 = mock(Order.class);

		when(order1.getOrderStatus()).thenReturn(new OrderStatus("test"));
		when(order2.getOrderStatus()).thenReturn(new OrderStatus("test2"));
		List<Order> orders = new ArrayList<>();
		orders.add(order1);
		orders.add(order2);

		GetAllListOrderByStatusResponse response = GetAllListOrderByStatusResponse.from(orders);

		when(orderServiceImpl.findByOrderStatus(statusId)).thenReturn(response);

		// Act & Assert
		mockMvc.perform(get("/api/orders/order-status/wait")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().json(objectMapper.writeValueAsString(response)));
	}

	// @Test
	// void getOrderStatusWait_NotFound() throws Exception {
	// 	// Arrange
	// 	when(orderServiceImpl.findByOrderStatus(anyLong())).thenThrow(new OrderFailException(
	// 		ErrorStatus.from("ORDER_STATUS_NOT_FOUND", HttpStatus.NOT_FOUND, LocalDateTime.now())
	// 	));
	//
	// 	// Act & Assert
	// 	mockMvc.perform(get("/api/order-status/wait")
	// 			.contentType(MediaType.APPLICATION_JSON))
	// 		.andExpect(status().isNotFound());
	// }

	@Test
	void getOrderStatusGoing_Success() throws Exception {
		// Arrange
		Long statusId = 4L;
		OrderStatus orderStatus = new OrderStatus("배송중");

		Order order1 = mock(Order.class);

		Order order2 = mock(Order.class);

		when(order1.getOrderStatus()).thenReturn(new OrderStatus("test"));
		when(order2.getOrderStatus()).thenReturn(new OrderStatus("test2"));

		List<Order> orders = new ArrayList<>();
		orders.add(order1);
		orders.add(order2);

		GetAllListOrderByStatusResponse response = GetAllListOrderByStatusResponse.from(orders);

		when(orderServiceImpl.findByOrderStatus(statusId)).thenReturn(response);

		// Act & Assert
		mockMvc.perform(get("/api/orders/order-status/going")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().json(objectMapper.writeValueAsString(response)));
	}

	@Test
	void getOrderStatusGoing_NotFound() throws Exception {
		// Arrange
		when(orderServiceImpl.findByOrderStatus(anyLong())).thenThrow(new OrderFailException(
			ErrorStatus.from("ORDER_STATUS_NOT_FOUND", HttpStatus.NOT_FOUND, LocalDateTime.now())
		));

		// Act & Assert
		mockMvc.perform(get("/api/orders/order-status/going")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isNotFound());
	}

	@Test
	void getOrderStatusComplete_Success() throws Exception {
		// Arrange
		Long statusId = 5L;
		OrderStatus orderStatus = new OrderStatus("배송 완료");

		Order order1 = mock(Order.class);

		Order order2 = mock(Order.class);

		when(order1.getOrderStatus()).thenReturn(new OrderStatus("test"));
		when(order2.getOrderStatus()).thenReturn(new OrderStatus("test2"));

		List<Order> orders = new ArrayList<>();
		orders.add(order1);
		orders.add(order2);

		GetAllListOrderByStatusResponse response = GetAllListOrderByStatusResponse.from(orders);

		when(orderServiceImpl.findByOrderStatus(statusId)).thenReturn(response);

		// Act & Assert
		mockMvc.perform(get("/api/orders/order-status/complete")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().json(objectMapper.writeValueAsString(response)));
	}

	@Test
	void getOrderStatusComplete_NotFound() throws Exception {
		// Arrange
		when(orderServiceImpl.findByOrderStatus(anyLong())).thenThrow(new OrderFailException(
			ErrorStatus.from("ORDER_STATUS_NOT_FOUND", HttpStatus.NOT_FOUND, LocalDateTime.now())
		));

		// Act & Assert
		mockMvc.perform(get("/api/orders/order-status/complete")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isNotFound());
	}

	@Test
	void getOrderStatusRefunded_Success() throws Exception {
		// Arrange
		Long statusId = 6L;
		OrderStatus orderStatus = new OrderStatus("반품");

		Order order1 = mock(Order.class);

		Order order2 = mock(Order.class);

		when(order1.getOrderStatus()).thenReturn(new OrderStatus("test"));
		when(order2.getOrderStatus()).thenReturn(new OrderStatus("test2"));

		List<Order> orders = new ArrayList<>();
		orders.add(order1);
		orders.add(order2);

		GetAllListOrderByStatusResponse response = GetAllListOrderByStatusResponse.from(orders);

		when(orderServiceImpl.findByOrderStatus(statusId)).thenReturn(response);

		// Act & Assert
		mockMvc.perform(get("/api/orders/order-status/refunded")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().json(objectMapper.writeValueAsString(response)));
	}

	@Test
	void refundedOrder_NotFound_Order() throws Exception {
		// Arrange
		String orderInfoId = "order123";

		when(orderRepository.findByOrderInfoId(orderInfoId)).thenReturn(null);

		// Act & Assert
		mockMvc.perform(get("/api/orders/refunded/{orderInfoId}", orderInfoId)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isNoContent());
	}

	@Test
	void refundedOrder_NotFound_Delivery() throws Exception {
		// Arrange
		String orderInfoId = "order123";

		Order mockOrder = mock(Order.class);
		when(orderRepository.findByOrderInfoId(orderInfoId)).thenReturn(mockOrder);
		when(deliveryRepository.findByOrder_OrderId(mockOrder.getOrderId())).thenReturn(null);

		// Act & Assert
		mockMvc.perform(get("/api/orders/refunded/{orderInfoId}", orderInfoId)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isNoContent());
	}

	@Test
	void refundedOrder_NotFound_DeliveryStatus() throws Exception {
		// Arrange
		String orderInfoId = "order123";

		Order mockOrder = mock(Order.class);
		Delivery mockDelivery = mock(Delivery.class);
		when(orderRepository.findByOrderInfoId(orderInfoId)).thenReturn(mockOrder);
		when(deliveryRepository.findByOrder_OrderId(mockOrder.getOrderId())).thenReturn(mockDelivery);
		when(deliveryStatusRepository.findDeliveryStatusByDeliveryStatusName("반품")).thenReturn(null);

		// Act & Assert
		mockMvc.perform(get("/api/orders/refunded/{orderInfoId}", orderInfoId)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isNoContent());
	}

	@Test
	void refundedOrder_NotFound_OrderStatus() throws Exception {
		// Arrange
		String orderInfoId = "order123";

		Order mockOrder = mock(Order.class);
		Delivery mockDelivery = mock(Delivery.class);
		DeliveryStatus mockDeliveryStatus = mock(DeliveryStatus.class);
		when(orderRepository.findByOrderInfoId(orderInfoId)).thenReturn(mockOrder);
		when(deliveryRepository.findByOrder_OrderId(mockOrder.getOrderId())).thenReturn(mockDelivery);
		when(deliveryStatusRepository.findDeliveryStatusByDeliveryStatusName("반품")).thenReturn(mockDeliveryStatus);
		when(orderStatusRepository.findByOrderStatusName("반품")).thenReturn(null);

		// Act & Assert
		mockMvc.perform(get("/api/orders/refunded/{orderInfoId}", orderInfoId)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isNoContent());
	}

	@Test
	void getOrderStatusRefunded_NotFound() throws Exception {
		when(orderServiceImpl.findByOrderStatus(anyLong())).thenThrow(new OrderFailException(
			ErrorStatus.from("ORDER_STATUS_NOT_FOUND", HttpStatus.NOT_FOUND, LocalDateTime.now())
		));

		mockMvc.perform(get("/api/orders/order-status/refunded")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isNotFound());
	}

	@Test
	void getOrderStatusRefunding_Success() throws Exception {
		// Arrange
		Long statusId = 7L;
		OrderStatus orderStatus = new OrderStatus("반품 요청중");

		Order order1 = mock(Order.class);

		Order order2 = mock(Order.class);

		when(order1.getOrderStatus()).thenReturn(new OrderStatus("test"));
		when(order2.getOrderStatus()).thenReturn(new OrderStatus("test2"));

		List<Order> orders = new ArrayList<>();
		orders.add(order1);
		orders.add(order2);

		GetAllListOrderByStatusResponse response = GetAllListOrderByStatusResponse.from(orders);

		when(orderServiceImpl.findByOrderStatus(statusId)).thenReturn(response);

		// Act & Assert
		mockMvc.perform(get("/api/orders/order-status/refunding")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().json(objectMapper.writeValueAsString(response)));
	}

	@Test
	void getOrderStatusRefunding_NotFound() throws Exception {
		// Arrange
		when(orderServiceImpl.findByOrderStatus(anyLong())).thenThrow(new OrderFailException(
			ErrorStatus.from("ORDER_STATUS_NOT_FOUND", HttpStatus.NOT_FOUND, LocalDateTime.now())
		));

		// Act & Assert
		mockMvc.perform(get("/api/orders/order-status/refunding")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isNotFound());
	}

	@Test
	void getOrderByInfoNon_Success() throws Exception {
		// Arrange
		String orderInfoId = "ORDER123";
		String email = "payer@example.com";

		OrderStatus orderStatus = new OrderStatus("반품 요청중");

		Order order = mock(Order.class);

		when(order.getOrderStatus()).thenReturn(new OrderStatus("test"));

		GetNonOrderByInfoResponse response = GetNonOrderByInfoResponse.from(order);

		when(orderServiceImpl.findByOrderInfoIdByEmail(orderInfoId, email)).thenReturn(response);

		OrderCheckNonRequest request = new OrderCheckNonRequest(orderInfoId, email);

		// Act & Assert
		mockMvc.perform(post("/api/orders/order-info/Non")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isOk());
	}

	@Test
	void getOrderByInfoNon_NotFound() throws Exception {
		// Arrange
		String orderInfoId = "1234125123";
		String email = "payer@example.com";

		when(orderServiceImpl.findByOrderInfoIdByEmail(anyString(), anyString())).thenThrow(new OrderFailException(
			ErrorStatus.from("ORDER_NOT_FOUND", HttpStatus.NOT_FOUND, LocalDateTime.now())
		));

		OrderCheckNonRequest request = new OrderCheckNonRequest(orderInfoId, email);

		// Act & Assert
		mockMvc.perform(post("/api/orders/order-info/Non")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isNotFound());
	}

	@Test
	void getUserPointOrders_Success() throws Exception {
		// Arrange
		Long userId = 1L;
		BigDecimal userPoints = new BigDecimal("100.00");
		User user = mock(User.class);

		GetUserPointOrderResponse response = GetUserPointOrderResponse.from(userPoints);

		CurrentUserDetails currentUserDetails = new CurrentUserDetails(
			new UserTokenInfo(userId, "password", Collections.emptyList(), "ACTIVE"));

		when(orderServiceImpl.getUserPoint(currentUserDetails)).thenReturn(response);

		// Act & Assert
		mockMvc.perform(get("/api/orders/orders-points")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk());
	}

	@Test
	void getBookByOneOrder_Success() throws Exception {
		// Arrange
		Long orderListId = 1L;
		Long bookId = 1L;
		BigDecimal bookPrice = new BigDecimal("29.99");
		List<Long> categoryId = Collections.singletonList(1L);

		GetBookByOrderCouponResponse response = GetBookByOrderCouponResponse.builder()
			.bookId(bookId)
			.bookPrice(bookPrice)
			.categoryId(categoryId)
			.build();

		when(bookOrderServiceImpl.getBookAndCategoryByOrderListId(orderListId)).thenReturn(response);

		// Act & Assert
		mockMvc.perform(get("/api/orders/{orderListId}/book", orderListId)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.bookId").value(bookId))
			.andExpect(jsonPath("$.bookPrice").value(bookPrice.toString()))
			.andExpect(jsonPath("$.categoryId[0]").value(categoryId.get(0)));
	}

	@Test
	void refundingOrder_Success() throws Exception {

		String orderInfoId = "order123";

		mockMvc.perform(get("/api/orders/refunding/{orderInfoId}", orderInfoId)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isNoContent());
	}

	@Test
	void refundedOrder_Success() throws Exception {
		String orderInfoId = "order123";
		Order order = mock(Order.class);
		Delivery delivery = mock(Delivery.class);
		DeliveryStatus deliveryStatus = mock(DeliveryStatus.class);
		OrderStatus orderStatus = mock(OrderStatus.class);

		when(orderRepository.findByOrderInfoId(orderInfoId)).thenReturn(order);
		when(deliveryRepository.findByOrder_OrderId(order.getOrderId())).thenReturn(delivery);
		when(deliveryStatusRepository.findDeliveryStatusByDeliveryStatusName("반품")).thenReturn(deliveryStatus);
		when(orderStatusRepository.findByOrderStatusName("반품")).thenReturn(orderStatus);

		mockMvc.perform(get("/api/orders/refunded/{orderInfoId}", orderInfoId)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isNoContent());
	}

	@Test
	void getRefundPolicy_Success() throws Exception {
		// Arrange
		RefundPolicy refundPolicy1 = new RefundPolicy(1L, "Refund for product", 30);
		RefundPolicy refundPolicy2 = new RefundPolicy(2L, "Refund for service", 15);
		List<RefundPolicy> refundPolicies = Arrays.asList(refundPolicy1, refundPolicy2);

		GetRefundResponse response1 = GetRefundResponse.from(refundPolicy1);
		GetRefundResponse response2 = GetRefundResponse.from(refundPolicy2);

		GetAllRefundResponse response = GetAllRefundResponse.builder()
			.refunds(Arrays.asList(response1, response2))
			.build();

		when(refundPolicyServiceImpl.getAllRefundPolicies()).thenReturn(response);

		// Act & Assert
		mockMvc.perform(get("/api/orders/refund-policy")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.refunds[0].refundPolicyId").value(1L))
			.andExpect(jsonPath("$.refunds[0].refundPolicyContent").value("Refund for product"))
			.andExpect(jsonPath("$.refunds[0].refundPolicyDate").value(30))
			.andExpect(jsonPath("$.refunds[1].refundPolicyId").value(2L))
			.andExpect(jsonPath("$.refunds[1].refundPolicyContent").value("Refund for service"))
			.andExpect(jsonPath("$.refunds[1].refundPolicyDate").value(15));
	}

	@Test
	void getRefundPolicy_NoPolicies() throws Exception {
		// Arrange
		when(refundPolicyServiceImpl.getAllRefundPolicies()).thenReturn(GetAllRefundResponse.builder()
			.refunds(Arrays.asList())
			.build());

		// Act & Assert
		mockMvc.perform(get("/api/orders/refund-policy")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.refunds").isEmpty());
	}

	@Test
	void updateRefundPolicy_Success() throws Exception {
		// Arrange
		Long refundPolicyId = 1L;
		UpdateRefundPolicyRequest request = new UpdateRefundPolicyRequest(
			"Updated refund policy content",
			45
		);

		RefundPolicy refundPolicy = new RefundPolicy(refundPolicyId, "Original content", 30);

		when(refundPolicyRepository.findById(refundPolicyId)).thenReturn(Optional.of(refundPolicy));
		doNothing().when(refundPolicyServiceImpl).updateRefundPolicy(any(UpdateRefundPolicyRequest.class), anyLong());

		// Act & Assert
		mockMvc.perform(put("/api/orders/refund-policy/{refundPolicyId}", refundPolicyId)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isNoContent());
	}

	@Test
	void updateRefundPolicy_NotContent() throws Exception {
		// Arrange
		Long refundPolicyId = 1L;
		UpdateRefundPolicyRequest request = new UpdateRefundPolicyRequest(
			"Updated refund policy content",
			45
		);

		// Mock repository to return an empty Optional
		when(refundPolicyRepository.findById(refundPolicyId)).thenReturn(Optional.empty());

		// Act & Assert
		mockMvc.perform(put("/api/orders/refund-policy/{refundPolicyId}", refundPolicyId)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isNoContent());
	}

	@Test
	void createRefundPolicy_Success() throws Exception {
		CreateRefundPolicyRequest createRefundPolicyRequest = new CreateRefundPolicyRequest(
			"New refund policy content",
			30
		);
		when(refundPolicyRepository.save(any(RefundPolicy.class))).thenAnswer(invocation -> null);

		mockMvc.perform(post("/api/orders/refund-policy")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(createRefundPolicyRequest)))
			.andExpect(status().isNoContent());
	}

	// @Test
	// void createRefundPolicy_BadRequest() throws Exception {
	//
	// 	CreateRefundPolicyRequest invalidRequest = new CreateRefundPolicyRequest(
	// 		"", 0
	// 	);
	//
	// 	mockMvc.perform(post("/api/orders/refund-policy")
	// 			.contentType(MediaType.APPLICATION_JSON)
	// 			.content(objectMapper.writeValueAsString(invalidRequest)))
	// 		.andExpect(status().isBadRequest()); // 400 Bad Request
	// }

	@Test
	void deleteRefundPolicy_Success() throws Exception {
		Long refundPolicyId = 1L;

		doNothing().when(refundPolicyRepository).deleteById(refundPolicyId);

		mockMvc.perform(delete("/api/orders/refund-policy/{refundPolicyId}", refundPolicyId)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isNoContent());
	}

	@Test
	void createCartOrders_Success() throws Exception {
		Long userId = 1L;
		String orderInfoId = UUID.randomUUID().toString();
		Order order = Order.builder()
			.orderInfoId(orderInfoId)
			.build();
		CreateCartOrderResponse response = CreateCartOrderResponse.from(order);
		CurrentUserDetails currentUserDetails = new CurrentUserDetails(
			new UserTokenInfo(userId, "password", Collections.emptyList(), "ACTIVE"));

		when(userRepository.getReferenceById(userId)).thenReturn(mock(User.class));
		when(orderRepository.save(any(Order.class))).thenReturn(order);
		when(orderServiceImpl.createCartOrder(currentUserDetails)).thenReturn(response);

		mockMvc.perform(get("/api/orders/cart-orders")
				.header("Authorization", "Bearer token")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk());
	}

	@Test
	void updateCartOrder_Success() throws Exception {
		// Arrange
		Long orderId = 1L;
		CreateOrderRequest createOrderRequest = new CreateOrderRequest(
			"John Doe",
			"john.doe@example.com",
			"01012345678",
			"123 Street, City",
			BigDecimal.valueOf(10000),
			BigDecimal.valueOf(1000),
			BigDecimal.valueOf(500)
		);

		Order order = Order.builder()
			.orderInfoId("order-info-id")
			.orderPrice(BigDecimal.valueOf(10000))
			.orderPointSale(BigDecimal.valueOf(1000))
			.orderCouponSale(BigDecimal.valueOf(500))
			.build();

		CreateOrderResponse response = CreateOrderResponse.from(order);

		OrderStatus orderStatus = OrderStatus.builder()
			.orderStatusName("결제 대기")
			.build();

		when(orderStatusRepository.findAll()).thenReturn(List.of(orderStatus));
		when(orderRepository.findByOrderId(orderId)).thenReturn(order);
		when(orderRepository.save(any(Order.class))).thenReturn(order);
		when(orderServiceImpl.updateCartOrder(createOrderRequest, orderId)).thenReturn(response);

		mockMvc.perform(put("/api/orders/cart-order/{orderId}", orderId)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(createOrderRequest)))
			.andExpect(status().isOk())
			.andExpect(content().json(objectMapper.writeValueAsString(response)));
	}
}
