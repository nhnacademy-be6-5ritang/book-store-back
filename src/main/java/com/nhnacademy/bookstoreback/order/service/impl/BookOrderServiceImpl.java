package com.nhnacademy.bookstoreback.order.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nhnacademy.bookstoreback.book.domain.entity.Book;
import com.nhnacademy.bookstoreback.book.repository.BookRepository;
import com.nhnacademy.bookstoreback.global.exception.BookOrderFailException;
import com.nhnacademy.bookstoreback.global.exception.OrderFailException;
import com.nhnacademy.bookstoreback.global.exception.payload.ErrorStatus;
import com.nhnacademy.bookstoreback.order.domain.dto.request.CreateBookOrderRequest;
import com.nhnacademy.bookstoreback.order.domain.dto.response.CreateBookOrderGetBookResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.CreateBookOrderGetOrderResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.CreateBookOrderResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.GetBookByOrderCouponResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.GetBookOrderGetBookResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.GetBookOrderResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.UpdateBookOrderResponse;
import com.nhnacademy.bookstoreback.order.domain.entity.BookOrder;
import com.nhnacademy.bookstoreback.order.domain.entity.Order;
import com.nhnacademy.bookstoreback.order.repository.BookOrderRepository;
import com.nhnacademy.bookstoreback.order.repository.OrderRepository;
import com.nhnacademy.bookstoreback.order.service.BookOrderService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class BookOrderServiceImpl implements BookOrderService {

	private final BookOrderRepository bookOrderRepository;
	private final OrderRepository orderRepository;
	private final BookRepository bookRepository;

	public static final String ERROR_ORDER_EXITS = "주문 정보를 찾을 수 없습니다";
	public static final String ERROR_BOOKORDER_EXITS = "주문 리스트를 찾을 수 없습니다";

	@Override
	public CreateBookOrderResponse createBookOrder(CreateBookOrderRequest createBookOrderRequest) {
		Book book = bookRepository.getReferenceById(createBookOrderRequest.bookId());
		Order order = null;
		if (createBookOrderRequest.orderId() != null) {
			order = orderRepository.getReferenceById(createBookOrderRequest.orderId());
		}
		BookOrder bookOrder = bookOrderRepository.save(
			BookOrder.toEntity(createBookOrderRequest.quantity(), book, order));

		//북 DTO 만들면 사용해서 수정
		return CreateBookOrderResponse.from(CreateBookOrderGetBookResponse.from(book),
			CreateBookOrderGetOrderResponse.from(order),
			createBookOrderRequest.quantity(), bookOrder.getOrderListId());
	}

	// 주문 생성시 업데이트
	@Override
	public UpdateBookOrderResponse updateOrder(Long bookOrderId, Long orderId) {
		BookOrder bookOrder;
		try {
			bookOrder = bookOrderRepository.getReferenceById(bookOrderId);
		} catch (EntityNotFoundException e) {
			ErrorStatus errorStatus = ErrorStatus.from(
				ERROR_BOOKORDER_EXITS,
				HttpStatus.UNPROCESSABLE_ENTITY,
				LocalDateTime.now()
			);
			throw new BookOrderFailException(errorStatus);
		}
		Order order = null;
		try {
			order = orderRepository.getReferenceById(orderId);
		} catch (EntityNotFoundException e) {
			ErrorStatus errorStatus = ErrorStatus.from(
				ERROR_ORDER_EXITS,
				HttpStatus.UNPROCESSABLE_ENTITY,
				LocalDateTime.now()
			);
			throw new OrderFailException(errorStatus);
		}
		bookOrder.update(order);
		bookOrderRepository.save(bookOrder);
		return UpdateBookOrderResponse.from(bookOrder);
	}

	@Override
	@Transactional(readOnly = true)
	public GetBookOrderResponse getBookOrder(Long bookOrderId) {
		BookOrder bookOrder;
		try {
			bookOrder = bookOrderRepository.getReferenceById(bookOrderId);
		} catch (EntityNotFoundException e) {
			ErrorStatus errorStatus = ErrorStatus.from(
				ERROR_BOOKORDER_EXITS,
				HttpStatus.UNPROCESSABLE_ENTITY,
				LocalDateTime.now()
			);
			throw new BookOrderFailException(errorStatus);
		}
		return GetBookOrderResponse.from(GetBookOrderGetBookResponse.from(bookOrder.getBook()),
			bookOrder.getBookQuantity(), bookOrder.getOrderListId());
	}

	@Override
	@Transactional(readOnly = true)
	public GetBookByOrderCouponResponse getBookAndCategoryByOrderListId(Long orderListId) {
		return bookOrderRepository.findBooksByOrderListId(orderListId);
	}

	@Override
	@Transactional(readOnly = true)
	public List<GetBookOrderResponse> getBookOrderByOrderId(String orderInfoId) {
		List<BookOrder> bookOrder = bookOrderRepository.findByOrder_OrderInfoId(orderInfoId);
		List<GetBookOrderResponse> bookOrderResponses = new ArrayList<>();
		for (BookOrder bookOrderItem : bookOrder) {
			bookOrderResponses.add(GetBookOrderResponse.from(GetBookOrderGetBookResponse.from(bookOrderItem.getBook()),
				bookOrderItem.getBookQuantity(), bookOrderItem.getOrderListId(),
				bookOrderItem.getOrder().getOrderId()));
		}
		return bookOrderResponses;
	}
}