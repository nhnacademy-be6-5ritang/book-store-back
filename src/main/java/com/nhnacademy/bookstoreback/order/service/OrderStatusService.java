package com.nhnacademy.bookstoreback.order.service;

import java.util.List;

import com.nhnacademy.bookstoreback.order.domain.dto.request.CreateOrderStatusRequest;
import com.nhnacademy.bookstoreback.order.domain.dto.response.GetOrderStatusResponse;

/**
 * @author 김다운
 * 주문 상태에 관련된 서비스 인터페이스입니다.
 */
public interface OrderStatusService {
	/**
	 * 주문 상태 만들기
	 * @param createOrderStatusRequest 주문 상태 이름
	 * @return 주문 상태 이름 리턴
	 */
	GetOrderStatusResponse create(CreateOrderStatusRequest createOrderStatusRequest);

	/**
	 * 주문 상태 업데이트
	 * @param createOrderStatusRequest 주문 상태 이름
	 * @param id 주문 상태 아이디
	 * @return 주문 상태 이름
	 */
	GetOrderStatusResponse update(CreateOrderStatusRequest createOrderStatusRequest, Long id);

	/**
	 * 주문 상태 삭제
	 * @param id 주문 상태 아이디
	 */
	void delete(Long id);

	/**
	 * 주문 상태 아이디로 찾기
	 * @param id 주문 상태 아이디
	 * @return 주문 상태 이름
	 */
	GetOrderStatusResponse findById(Long id);

	/**
	 * 모든 주문 상태 가져오기
	 * @return 모든 주문 상태
	 */
	List<GetOrderStatusResponse> findAll();
}
