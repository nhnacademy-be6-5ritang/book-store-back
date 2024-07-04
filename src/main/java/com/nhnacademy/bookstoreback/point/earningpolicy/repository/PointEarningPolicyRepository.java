package com.nhnacademy.bookstoreback.point.earningpolicy.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nhnacademy.bookstoreback.point.earningpolicy.domain.entity.PointEarningPolicy;

public interface PointEarningPolicyRepository extends JpaRepository<PointEarningPolicy, Long> {
	boolean existsByPointEarningPolicyType(String pointEarningPolicyType);

	Optional<PointEarningPolicy> findByPointEarningPolicyType(String signUp);
}
