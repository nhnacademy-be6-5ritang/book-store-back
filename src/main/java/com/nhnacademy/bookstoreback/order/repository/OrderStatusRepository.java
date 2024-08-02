package com.nhnacademy.bookstoreback.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nhnacademy.bookstoreback.order.domain.entity.OrderStatus;

/**
 * @author 김다운
 * 주문 상태 데이터베이스 작업을 위한 JPA 리포지토리 인터페이스입니다.
 */
public interface OrderStatusRepository extends JpaRepository<OrderStatus, Long> {

	/**
	 * 주문 상태 이름으로 주문 상태를 조회합니다.
	 *
	 * @param orderStatusName 주문 상태 이름
	 * @return 주문 상태 이름에 해당하는 {@link OrderStatus} 객체
	 */
	OrderStatus findByOrderStatusName(String orderStatusName);
}
