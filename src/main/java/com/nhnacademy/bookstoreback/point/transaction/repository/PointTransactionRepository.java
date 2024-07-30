package com.nhnacademy.bookstoreback.point.transaction.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.nhnacademy.bookstoreback.point.transaction.domain.entity.PointTransaction;

/**
 * @author 김태환
 * PointTransaction 엔티티를 관리하는 Spring Data JPA 리포지토리입니다.
 */
public interface PointTransactionRepository extends JpaRepository<PointTransaction, Long> {
	/**
	 * 특정 사용자의 모든 포인트 거래 내역을 페이지네이션하여 조회합니다.
	 *
	 * @param userId 사용자의 ID
	 * @param pageable 페이지 요청 정보 (페이지 번호, 페이지 크기, 정렬 조건)
	 * @return 특정 사용자의 포인트 거래 내역 페이지
	 */
	Page<PointTransaction> findAllByUserId(Long userId, Pageable pageable);
}
