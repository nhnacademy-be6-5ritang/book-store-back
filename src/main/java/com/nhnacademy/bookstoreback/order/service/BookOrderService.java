package com.nhnacademy.bookstoreback.order.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nhnacademy.bookstoreback.book.domain.entity.Book;
import com.nhnacademy.bookstoreback.book.repository.BookRepository;
import com.nhnacademy.bookstoreback.order.domain.dto.request.CreateBookOrderRequest;
import com.nhnacademy.bookstoreback.order.domain.dto.response.CreateBookOrderGetOrderResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.CreateBookOrderResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.GetBookOrderByInfoIdResponse;
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
		Order order = orderRepository.getReferenceById(createBookOrderRequest.orderId());
		bookOrderRepository.save(BookOrder.toEntity(createBookOrderRequest.quantity(), book, order));

		//북 DTO 만들면 사용해서 수정
		return CreateBookOrderResponse.from(book, CreateBookOrderGetOrderResponse.from(order),
			createBookOrderRequest.quantity());
	}

	//주문 보안 아이디로 주문리스트 가져오는 JPA
	@Transactional(readOnly = true)
	public GetBookOrderByInfoIdResponse findByOrderInfoId(String orderInfoId) {
		BookOrder bookOrder = bookOrderRepository.findByOrder_OrderInfoId(orderInfoId);
		return GetBookOrderByInfoIdResponse.from(bookOrder.getBook(),
			CreateBookOrderGetOrderResponse.from(bookOrder.getOrder()), bookOrder.getBookQuantity());
	}
}
