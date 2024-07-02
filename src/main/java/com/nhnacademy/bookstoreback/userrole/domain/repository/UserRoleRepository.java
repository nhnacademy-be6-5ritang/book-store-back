package com.nhnacademy.bookstoreback.userrole.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nhnacademy.bookstoreback.role.domain.entity.Role;
import com.nhnacademy.bookstoreback.user.domain.entity.User;
import com.nhnacademy.bookstoreback.userrole.domain.entity.UserRole;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
	List<UserRole> findByUser(User user);

	void deleteByUserAndRole(User user, Role role);

	boolean existsByUserAndRole(User user, Role role);
}
