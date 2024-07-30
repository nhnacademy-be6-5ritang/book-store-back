package com.nhnacademy.bookstoreback.order;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.nhnacademy.bookstoreback.book.domain.entity.Book;
import com.nhnacademy.bookstoreback.book.repository.BookRepository;
import com.nhnacademy.bookstoreback.global.exception.BookOrderFailException;
import com.nhnacademy.bookstoreback.global.exception.OrderFailException;
import com.nhnacademy.bookstoreback.order.domain.dto.request.CreateBookOrderRequest;
import com.nhnacademy.bookstoreback.order.domain.dto.response.CreateBookOrderResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.GetBookByOrderCouponResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.GetBookOrderResponse;
import com.nhnacademy.bookstoreback.order.domain.entity.BookOrder;
import com.nhnacademy.bookstoreback.order.domain.entity.Order;
import com.nhnacademy.bookstoreback.order.repository.BookOrderRepository;
import com.nhnacademy.bookstoreback.order.repository.OrderRepository;
import com.nhnacademy.bookstoreback.order.service.impl.BookOrderServiceImpl;

import jakarta.persistence.EntityNotFoundException;

class BookOrderServiceImplTest {

	@InjectMocks
	private BookOrderServiceImpl bookOrderService;

	@Mock
	private BookOrderRepository bookOrderRepository;

	@Mock
	private OrderRepository orderRepository;

	@Mock
	private BookRepository bookRepository;

	@BeforeEach
	void setup() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testCreateBookOrder() {
		CreateBookOrderRequest request = mock(CreateBookOrderRequest.class);
		Book book = mock(Book.class);
		Order order = mock(Order.class);
		BookOrder bookOrder = mock(BookOrder.class);

		when(request.bookId()).thenReturn(1L);
		when(request.orderId()).thenReturn(1L);
		when(request.quantity()).thenReturn(2);
		when(bookRepository.getReferenceById(1L)).thenReturn(book);
		when(orderRepository.getReferenceById(1L)).thenReturn(order);
		when(bookOrderRepository.save(any(BookOrder.class))).thenReturn(bookOrder);
		when(bookOrder.getOrderListId()).thenReturn(123L);

		CreateBookOrderResponse response = bookOrderService.createBookOrder(request);

		assertThat(response).isNotNull();
		verify(bookRepository).getReferenceById(1L);
		verify(orderRepository).getReferenceById(1L);
		verify(bookOrderRepository).save(any(BookOrder.class));
	}

	@Test
	void testUpdateOrder_BookOrderNotFound() {
		when(bookOrderRepository.getReferenceById(1L)).thenThrow(EntityNotFoundException.class);

		assertThrows(BookOrderFailException.class, () -> bookOrderService.updateOrder(1L, 1L));

		verify(bookOrderRepository).getReferenceById(1L);
		verify(orderRepository, never()).getReferenceById(anyLong());
	}

	@Test
	void testUpdateOrder_OrderNotFound() {
		BookOrder bookOrder = mock(BookOrder.class);

		when(bookOrderRepository.getReferenceById(1L)).thenReturn(bookOrder);
		when(orderRepository.getReferenceById(1L)).thenThrow(EntityNotFoundException.class);

		assertThrows(OrderFailException.class, () -> bookOrderService.updateOrder(1L, 1L));

		verify(bookOrderRepository).getReferenceById(1L);
		verify(orderRepository).getReferenceById(1L);
		verify(bookOrder, never()).update(any(Order.class));
		verify(bookOrderRepository, never()).save(any(BookOrder.class));
	}

	@Test
	void testGetBookOrder() {
		BookOrder bookOrder = mock(BookOrder.class);
		Book book = mock(Book.class);

		when(bookOrderRepository.getReferenceById(1L)).thenReturn(bookOrder);
		when(bookOrder.getBook()).thenReturn(book);
		when(bookOrder.getBookQuantity()).thenReturn(2);
		when(bookOrder.getOrderListId()).thenReturn(123L);

		GetBookOrderResponse response = bookOrderService.getBookOrder(1L);

		assertThat(response).isNotNull();
		verify(bookOrderRepository).getReferenceById(1L);
	}

	@Test
	void testGetBookOrder_NotFound() {
		when(bookOrderRepository.getReferenceById(1L)).thenThrow(EntityNotFoundException.class);

		assertThrows(BookOrderFailException.class, () -> bookOrderService.getBookOrder(1L));

		verify(bookOrderRepository).getReferenceById(1L);
	}

	@Test
	void testGetBookAndCategoryByOrderListId() {
		GetBookByOrderCouponResponse response = mock(GetBookByOrderCouponResponse.class);

		when(bookOrderRepository.findBooksByOrderListId(1L)).thenReturn(response);

		GetBookByOrderCouponResponse result = bookOrderService.getBookAndCategoryByOrderListId(1L);

		assertThat(result).isNotNull();
		verify(bookOrderRepository).findBooksByOrderListId(1L);
	}

	@Test
	void testGetBookOrderByOrderId() {
		BookOrder bookOrder = mock(BookOrder.class);
		Book book = mock(Book.class);
		Order order = mock(Order.class);

		when(bookOrderRepository.findByOrder_OrderInfoId("order123")).thenReturn(Collections.singletonList(bookOrder));
		when(bookOrder.getBook()).thenReturn(book);
		when(bookOrder.getBookQuantity()).thenReturn(2);
		when(bookOrder.getOrderListId()).thenReturn(123L);
		when(bookOrder.getOrder()).thenReturn(order);
		when(order.getOrderId()).thenReturn(1L);

		List<GetBookOrderResponse> responses = bookOrderService.getBookOrderByOrderId("order123");

		assertThat(responses).isNotEmpty();
		verify(bookOrderRepository).findByOrder_OrderInfoId("order123");
	}

	@Test
	void testGetBookOrderByOrderId_EmptyList() {
		when(bookOrderRepository.findByOrder_OrderInfoId("order123")).thenReturn(Collections.emptyList());

		List<GetBookOrderResponse> responses = bookOrderService.getBookOrderByOrderId("order123");

		assertThat(responses).isEmpty();
		verify(bookOrderRepository).findByOrder_OrderInfoId("order123");
	}
}
