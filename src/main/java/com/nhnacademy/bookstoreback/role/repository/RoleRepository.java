package com.nhnacademy.bookstoreback.role.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nhnacademy.bookstoreback.role.domain.entity.Role;

/**
 * @author 김태환
 * 역할 엔티티를 관리하는 Spring Data JPA 리포지토리입니다.
 */
public interface RoleRepository extends JpaRepository<Role, Long> {
	/**
	 * 주어진 역할 이름이 존재하는지 확인합니다.
	 *
	 * @param roleName 확인할 역할의 이름
	 * @return 주어진 역할 이름이 존재하면 {@code true}, 그렇지 않으면 {@code false}
	 */
	boolean existsByRoleName(String roleName);

	/**
	 * 주어진 역할 이름으로 역할을 삭제합니다.
	 *
	 * @param roleName 삭제할 역할의 이름
	 */
	void deleteByRoleName(String roleName);

	/**
	 * 주어진 역할 이름으로 역할을 조회합니다.
	 *
	 * @param roleName 조회할 역할의 이름
	 * @return 주어진 역할 이름에 해당하는 역할이 존재하면 {@link Optional<Role>}, 존재하지 않으면 빈 {@link Optional}
	 */
	Optional<Role> findByRoleName(String roleName);
}
