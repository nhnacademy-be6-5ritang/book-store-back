package com.nhnacademy.bookstoreback.order.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nhnacademy.bookstoreback.order.domain.entity.BookOrder;

/**
 * @author 김다운
 * 엔티티에 대한 데이터베이스 작업을 수행하는 JPA 리포지토리입니다.
 */
public interface BookOrderRepository extends JpaRepository<BookOrder, Long>, CustomBookOrderRepository {

	/**
	 * 주어진 주문 ID로 BookOrder 를 찾습니다.
	 *
	 * @param orderId 주문 ID
	 * @return 주문 ID에 해당하는 BookOrder 엔티티
	 */
	BookOrder findByOrder_OrderId(Long orderId);

	/**
	 * 주어진 주문 정보 ID로 BookOrder 목록을 찾습니다.
	 *
	 * @param orderId 주문 정보 ID
	 * @return 주문 정보 ID에 해당하는 BookOrder 엔티티 목록
	 */
	List<BookOrder> findByOrder_OrderInfoId(String orderId);

	/**
	 * 주어진 사용자 ID와 주문 상태명으로 BookOrder 목록을 찾습니다.
	 *
	 * @param userId 사용자 ID
	 * @param orderStatus 주문 상태명
	 * @return 사용자 ID와 주문 상태명에 해당하는 BookOrder 엔티티 목록
	 */
	List<BookOrder> findAllByOrder_User_IdAndOrder_OrderStatus_OrderStatusName(Long userId, String orderStatus);

}
