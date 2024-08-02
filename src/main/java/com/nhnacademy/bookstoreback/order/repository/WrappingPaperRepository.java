package com.nhnacademy.bookstoreback.order.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nhnacademy.bookstoreback.order.domain.entity.WrappingPaper;

/**
 * @author 김다운
 * WrappingPaper 엔티티에 대한 데이터베이스 작업을 수행하기 위한 JPA 리포지토리입니다.
 */
public interface WrappingPaperRepository extends JpaRepository<WrappingPaper, Long> {

	/**
	 * 지정된 주문 ID와 관련된 포장지 목록을 조회합니다.
	 *
	 * @param orderId 주문 ID
	 * @return 지정된 주문 ID와 관련된 포장지 목록
	 */
	List<WrappingPaper> findAllByBookOrder_OrderListId(Long orderId);
}
