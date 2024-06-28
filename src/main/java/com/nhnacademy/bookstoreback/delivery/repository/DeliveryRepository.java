package com.nhnacademy.bookstoreback.delivery.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.nhnacademy.bookstoreback.delivery.domain.entity.Delivery;

/**
 * @author 이경헌
 * Delivery 엔티티에 접근하기 위한 JPA 레포지토리입니다.
 */
public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
	/**
	 * 특정 사용자의 모든 배송 목록을 페이징하여 조회합니다.
	 *
	 * @param userId   사용자의 ID
	 * @param pageable 페이지 요청 정보 (페이지 번호, 페이지 크기, 정렬 조건)
	 * @return 특정 사용자의 배송 목록 페이지
	 */
	Page<Delivery> findAllByOrder_Cart_User_Id(Long userId, Pageable pageable);
}
