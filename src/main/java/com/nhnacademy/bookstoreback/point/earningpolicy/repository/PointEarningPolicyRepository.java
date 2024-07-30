package com.nhnacademy.bookstoreback.point.earningpolicy.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nhnacademy.bookstoreback.point.earningpolicy.domain.entity.PointEarningPolicy;

/**
 * @author 김태환
 * PointEarningPolicy 엔티티를 관리하는 Spring Data JPA 리포지토리입니다.
 */
public interface PointEarningPolicyRepository extends JpaRepository<PointEarningPolicy, Long> {
	/**
	 * 주어진 포인트 적립 정책 타입이 존재하는지 확인합니다.
	 *
	 * @param pointEarningPolicyType 확인할 포인트 적립 정책 타입
	 * @return 주어진 포인트 적립 정책 타입이 존재하면 {@code true}, 그렇지 않으면 {@code false}
	 */
	boolean existsByPointEarningPolicyType(String pointEarningPolicyType);

	/**
	 * 주어진 포인트 적립 정책 타입에 해당하는 포인트 적립 정책을 조회합니다.
	 *
	 * @param pointEarningPolicyType 조회할 포인트 적립 정책 타입
	 * @return 주어진 포인트 적립 정책 타입에 해당하는 포인트 적립 정책이 존재하면 {@code Optional}에 감싸서 반환하고, 존재하지 않으면 {@code Optional.empty()}를 반환합니다.
	 */
	Optional<PointEarningPolicy> findByPointEarningPolicyType(String pointEarningPolicyType);

}
