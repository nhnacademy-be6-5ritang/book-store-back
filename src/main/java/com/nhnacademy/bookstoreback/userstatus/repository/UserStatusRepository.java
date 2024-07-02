package com.nhnacademy.bookstoreback.userstatus.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nhnacademy.bookstoreback.userstatus.domain.entity.UserStatus;

public interface UserStatusRepository extends JpaRepository<UserStatus, Long> {
	boolean existsByUserStatusName(String userStatusName);

	void deleteByUserStatusName(String userStatusName);
}
