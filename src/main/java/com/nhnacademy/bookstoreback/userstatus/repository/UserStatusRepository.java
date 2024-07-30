package com.nhnacademy.bookstoreback.userstatus.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nhnacademy.bookstoreback.userstatus.domain.entity.UserStatus;

/**
 * @author 김태환
 * UserStatus 엔티티를 위한 레포지토리 인터페이스입니다.
 */
public interface UserStatusRepository extends JpaRepository<UserStatus, Long> {
	/**
	 * 특정 사용자 상태 이름이 존재하는지 확인합니다.
	 *
	 * @param userStatusName 확인할 사용자 상태 이름
	 * @return 주어진 사용자 상태 이름이 존재하면 {@code true}, 그렇지 않으면 {@code false}
	 */
	boolean existsByUserStatusName(String userStatusName);

	/**
	 * 특정 사용자 상태 이름과 연관된 사용자 상태를 삭제합니다.
	 *
	 * @param userStatusName 삭제할 사용자 상태 이름
	 */
	void deleteByUserStatusName(String userStatusName);

	/**
	 * 특정 사용자 상태 이름에 대한 사용자 상태를 조회합니다.
	 *
	 * @param userStatusName 조회할 사용자 상태 이름
	 * @return 주어진 사용자 상태 이름과 연관된 {@link UserStatus} 객체의 Optional
	 */
	Optional<UserStatus> findByUserStatusName(String userStatusName);
}
