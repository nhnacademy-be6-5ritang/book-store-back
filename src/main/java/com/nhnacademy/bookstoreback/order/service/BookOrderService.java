package com.nhnacademy.bookstoreback.order.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nhnacademy.bookstoreback.book.domain.entity.Book;
import com.nhnacademy.bookstoreback.book.repository.BookRepository;
import com.nhnacademy.bookstoreback.order.domain.dto.request.CreateBookOrderRequest;
import com.nhnacademy.bookstoreback.order.domain.dto.response.CreateBookOrderGetBookResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.CreateBookOrderGetOrderResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.CreateBookOrderResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.GetBookOrderGetBookResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.GetBookOrderResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.UpdateBookOrderResponse;
import com.nhnacademy.bookstoreback.order.domain.entity.BookOrder;
import com.nhnacademy.bookstoreback.order.domain.entity.Order;
import com.nhnacademy.bookstoreback.order.repository.BookOrderRepository;
import com.nhnacademy.bookstoreback.order.repository.OrderRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class BookOrderService {

	private final BookOrderRepository bookOrderRepository;
	private final OrderRepository orderRepository;
	private final BookRepository bookRepository;

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

	// //주문 보안 아이디로 주문리스트 가져오는 JPA
	// @Transactional(readOnly = true)
	// public GetBookOrderByInfoIdResponse findByOrderInfoId(String orderInfoId) {
	// 	BookOrder bookOrder = bookOrderRepository.findByOrder_OrderInfoId(orderInfoId);
	// 	return GetBookOrderByInfoIdResponse.from(bookOrder.getOrderListId(),
	// 		FindByInfoIdBookOrderGetBookResponse.from(bookOrder.getBook()),
	// 		FindByInfoIdBookOrderGetOrderResponse.from(bookOrder.getOrder()), bookOrder.getBookQuantity());
	// }

	// 주문 생성시 업데이트
	public UpdateBookOrderResponse updateOrder(Long bookOrderId, Long orderId) {
		BookOrder bookOrder = bookOrderRepository.getReferenceById(bookOrderId);
		Order order = orderRepository.getReferenceById(orderId);
		bookOrder.update(order);
		bookOrderRepository.save(bookOrder);
		return UpdateBookOrderResponse.from(bookOrder);
	}

	public GetBookOrderResponse getBookOrder(Long bookOrderId) {
		BookOrder bookOrder = bookOrderRepository.getReferenceById(bookOrderId);
		return GetBookOrderResponse.from(GetBookOrderGetBookResponse.from(bookOrder.getBook()),
			bookOrder.getBookQuantity());
	}

}
