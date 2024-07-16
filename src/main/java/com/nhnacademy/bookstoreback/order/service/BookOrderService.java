package com.nhnacademy.bookstoreback.order.service;

import java.util.List;

import com.nhnacademy.bookstoreback.order.domain.dto.request.CreateBookOrderRequest;
import com.nhnacademy.bookstoreback.order.domain.dto.response.CreateBookOrderResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.GetBookByOrderCouponResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.GetBookOrderResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.UpdateBookOrderResponse;

public interface BookOrderService {

	/**
	 * 주문리스트 생성
	 * @param createBookOrderRequest 주문아이디 , 북아이디, 책 구매 개수
	 * @return 북과 주문 정보 책 구매 개수 및 pk값
	 */
	CreateBookOrderResponse createBookOrder(CreateBookOrderRequest createBookOrderRequest);

	/**
	 * 주문리스트 업데이트
	 * @param bookOrderId 주문리스트 아이디
	 * @param orderId 주문 아이디
	 * @return 일부 주문 정보 리턴
	 */
	UpdateBookOrderResponse updateOrder(Long bookOrderId, Long orderId);

	/**
	 * 주문 리스트 아이디로 주문 리스트 가져오기
	 * @param bookOrderId 주문리스트 아이디
	 * @return 주문 리스트 정보 리턴
	 */
	GetBookOrderResponse getBookOrder(Long bookOrderId);

	/**
	 * 주문 리스트 아이디로 도서정보 가져오기
	 * @author 이기훈
	 * @param orderListId 주문리스트 아이디
	 * @return 주문 리스트 정보 리턴
	 */
	GetBookByOrderCouponResponse getBookAndCategoryByOrderListId(Long orderListId);

	/**
	 * 주문 보안아이디로 주문리스트 가져오기
	 * @param orderInfoId 주문보안아이디
	 * @return 주문리스트 정보
	 */
	public List<GetBookOrderResponse> getBookOrderByOrderId(String orderInfoId);
}
