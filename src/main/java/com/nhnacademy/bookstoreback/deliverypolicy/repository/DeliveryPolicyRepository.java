package com.nhnacademy.bookstoreback.deliverypolicy.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nhnacademy.bookstoreback.deliverypolicy.domain.entity.DeliveryPolicy;

/**
 * @author 이경헌
 * DeliveryPolicy 엔티티에 접근하기 위한 JPA 레포지토리입니다.
 */
public interface DeliveryPolicyRepository extends JpaRepository<DeliveryPolicy, Long> {

	/**
	 * 주어진 이름의 배송비 정책이 저장소에 존재하는지 여부를 확인합니다.
	 *
	 * @param deliveryPolicyName 확인할 배송비 정책의 이름
	 * @return 주어진 이름의 배송비 정책이 존재하면 {@code true}, 그렇지 않으면 {@code false}
	 */
	boolean existsByDeliveryPolicyName(String deliveryPolicyName);

	/**
	 * 주어진 주문 가격보다 작거나 같은 배송비 정책을 표준 가격 내림차순으로 정렬하여 배송비 정책을 표준 가격이 가장 높은 배송비 정책을 조회합니다.
	 *
	 * @param orderPrice 주문 가격.
	 * @return 주문 가격에 맞는 가장 높은 표준 가격을 가진 배송비 정책.
	 */
	List<DeliveryPolicy> findByDeliveryPolicyStandardPriceLessThanEqualOrderByDeliveryPolicyStandardPriceDesc(
		BigDecimal orderPrice);
}
