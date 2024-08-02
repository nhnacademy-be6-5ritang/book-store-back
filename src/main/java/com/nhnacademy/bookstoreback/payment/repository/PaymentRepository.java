package com.nhnacademy.bookstoreback.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nhnacademy.bookstoreback.payment.dto.entitiy.Payment;

/**
 * @author 김다운
 * {@code Payment} 엔티티에 대한 데이터베이스 작업을 수행하기 위한 JPA 리포지토리입니다.
 */
public interface PaymentRepository extends JpaRepository<Payment, Long> {

	/**
	 * 지정된 주문 정보 ID에 해당하는 결제 정보를 조회합니다.
	 *
	 * @param orderInfoId 주문 정보 ID
	 * @return 해당 주문 정보 ID에 대한 결제 정보, 없으면 {@code null}
	 */
	Payment findByOrder_OrderInfoId(String orderInfoId);
}
