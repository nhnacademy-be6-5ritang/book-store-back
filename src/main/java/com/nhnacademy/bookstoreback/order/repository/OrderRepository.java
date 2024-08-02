package com.nhnacademy.bookstoreback.order.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.nhnacademy.bookstoreback.order.domain.entity.Order;

/**
 * @author 김다운
 * 주문 데이터베이스 작업을 위한 JPA 리포지토리 인터페이스입니다.
 */
public interface OrderRepository extends JpaRepository<Order, Long> {

	/**
	 * 주문 상태 ID로 주문을 페이지 단위로 조회합니다.
	 *
	 * @param orderStatusId 주문 상태 ID
	 * @param page 페이징 정보
	 * @return 주문 상태 ID에 해당하는 주문 목록의 페이지
	 */
	Page<Order> findByOrderStatus_OrderStatusId(Long orderStatusId, Pageable page);

	/**
	 * 사용자 ID로 모든 주문을 조회합니다.
	 *
	 * @param userId 사용자 ID
	 * @return 사용자 ID에 해당하는 모든 주문 목록
	 */
	List<Order> findAllByUserId(Long userId);

	/**
	 * 주문 정보 ID로 주문을 조회합니다.
	 *
	 * @param orderInfoId 주문 정보 ID
	 * @return 주문 정보 ID에 해당하는 주문
	 */
	Order findByOrderInfoId(String orderInfoId);

	/**
	 * 주문 ID로 주문을 조회합니다.
	 *
	 * @param orderId 주문 ID
	 * @return 주문 ID에 해당하는 주문
	 */
	Order findByOrderId(Long orderId);

	/**
	 * 주문 상태 ID로 모든 주문을 조회합니다.
	 *
	 * @param orderStatusId 주문 상태 ID
	 * @return 주문 상태 ID에 해당하는 모든 주문 목록
	 */
	List<Order> findAllByOrderStatus_OrderStatusId(Long orderStatusId);

	/**
	 * 사용자 ID로 주문을 페이지 단위로 조회합니다.
	 *
	 * @param userId 사용자 ID
	 * @param page 페이징 정보
	 * @return 사용자 ID에 해당하는 주문 목록의 페이지
	 */
	Page<Order> findAllByUser_Id(Long userId, Pageable page);
}
