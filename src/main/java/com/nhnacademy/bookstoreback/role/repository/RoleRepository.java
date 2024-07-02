package com.nhnacademy.bookstoreback.role.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nhnacademy.bookstoreback.role.domain.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
	boolean existsByRoleName(String roleName);

	void deleteByRoleName(String roleName);
}
