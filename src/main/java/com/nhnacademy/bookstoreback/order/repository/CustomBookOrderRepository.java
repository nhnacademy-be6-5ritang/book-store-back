package com.nhnacademy.bookstoreback.order.repository;

import com.nhnacademy.bookstoreback.order.domain.dto.response.GetBookByOrderCouponResponse;

/**
 * @author 김다운
 * 주문 관련 사용자 정의 데이터베이스 작업을 위한 리포지토리 인터페이스입니다.
 */
public interface CustomBookOrderRepository {

	/**
	 * 주어진 주문 목록 ID로 책 정보와 쿠폰 정보를 조회합니다.
	 *
	 * @param orderListId 주문 목록 ID
	 * @return 주문 목록 ID에 해당하는 책과 쿠폰 정보
	 */
	GetBookByOrderCouponResponse findBooksByOrderListId(Long orderListId);
}
