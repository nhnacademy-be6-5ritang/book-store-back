package com.nhnacademy.bookstoreback.usergrade.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nhnacademy.bookstoreback.usergrade.domain.entity.UserGrade;

/**
 * @author 김태환
 * UserGrade 엔티티를 위한 레포지토리 인터페이스입니다.
 */
public interface UserGradeRepository extends JpaRepository<UserGrade, String> {
	/**
	 * 특정 사용자 등급명이 존재하는지 확인합니다.
	 *
	 * @param userGradeName 확인할 사용자 등급명
	 * @return 주어진 사용자 등급명이 존재하면 {@code true}, 그렇지 않으면 {@code false}
	 */
	boolean existsByUserGradeName(String userGradeName);

	/**
	 * 특정 사용자 등급명을 사용하여 사용자 등급을 조회합니다.
	 *
	 * @param userGradeName 조회할 사용자 등급명
	 * @return 주어진 사용자 등급명을 가진 {@link UserGrade} 객체를 감싸는 Optional 객체
	 */
	Optional<UserGrade> findByUserGradeName(String userGradeName);
}
