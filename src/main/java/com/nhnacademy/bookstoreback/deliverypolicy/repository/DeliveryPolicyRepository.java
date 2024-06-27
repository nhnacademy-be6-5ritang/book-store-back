package com.nhnacademy.bookstoreback.deliverypolicy.repository;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nhnacademy.bookstoreback.deliverypolicy.domain.entity.DeliveryPolicy;

/**
 * @author 이경헌
 * DeliveryPolicy 엔티티에 접근하기 위한 JPA 레포지토리입니다.
 */
public interface DeliveryPolicyRepository extends JpaRepository<DeliveryPolicy, Long> {
	/**
	 * 주어진 주문 가격보다 작거나 같은 배송비 정책을 표준 가격 내림차순으로 정렬하여 배송비 정책을 표준 가격이 가장 높은 배송비 정책을 조회합니다.
	 *
	 * @param orderPrice 주문 가격.
	 * @return 주문 가격에 맞는 가장 높은 표준 가격을 가진 배송비 정책.
	 */
	DeliveryPolicy findByDeliveryPolicyStandardPriceLessThanEqualOrderByDeliveryPolicyStandardPriceDesc(
		BigDecimal orderPrice);
}
