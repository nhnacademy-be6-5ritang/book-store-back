package com.nhnacademy.bookstoreback.userrole.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nhnacademy.bookstoreback.role.domain.entity.Role;
import com.nhnacademy.bookstoreback.user.domain.entity.User;
import com.nhnacademy.bookstoreback.userrole.domain.entity.UserRole;

/**
 * @author 김태환
 * UserRole 엔티티를 위한 레포지토리 인터페이스입니다.
 */
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
	/**
	 * 특정 사용자에 대한 모든 사용자 역할을 조회합니다.
	 *
	 * @param user 조회할 사용자의 엔티티
	 * @return 주어진 사용자와 연관된 {@link UserRole} 객체의 목록
	 */
	List<UserRole> findByUser(User user);

	/**
	 * 특정 사용자와 역할에 대한 사용자 역할을 삭제합니다.
	 *
	 * @param user 삭제할 사용자 엔티티
	 * @param role 삭제할 역할 엔티티
	 */
	void deleteByUserAndRole(User user, Role role);

	/**
	 * 특정 사용자와 역할에 대한 사용자 역할의 존재 여부를 확인합니다.
	 *
	 * @param user 확인할 사용자 엔티티
	 * @param role 확인할 역할 엔티티
	 * @return 주어진 사용자와 역할에 대해 사용자 역할이 존재하면 {@code true}, 그렇지 않으면 {@code false}
	 */
	boolean existsByUserAndRole(User user, Role role);
}
