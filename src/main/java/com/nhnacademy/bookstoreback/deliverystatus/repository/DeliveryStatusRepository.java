package com.nhnacademy.bookstoreback.deliverystatus.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nhnacademy.bookstoreback.deliverystatus.domain.entity.DeliveryStatus;

/**
 * @author 이경헌
 * DeliveryStatus 엔티티에 접근하기 위한 JPA 레포지토리입니다.
 */
public interface DeliveryStatusRepository extends JpaRepository<DeliveryStatus, Long> {
	/**
	 * 배송 상태 이름을 기준으로 배송 상태 엔티티를 검색합니다.
	 *
	 * @param deliveryStatusName 검색할 배송 상태의 이름
	 * @return 해당 이름에 해당하는 배송 상태 엔티티
	 */
	DeliveryStatus findDeliveryStatusByDeliveryStatusName(String deliveryStatusName);
}
