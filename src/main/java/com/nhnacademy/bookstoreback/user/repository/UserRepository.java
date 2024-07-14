package com.nhnacademy.bookstoreback.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nhnacademy.bookstoreback.user.domain.entity.User;

public interface UserRepository extends JpaRepository<User, Long>, CustomUserRepository {
	boolean existsByEmail(String email);

	User findByEmail(String userEmail);

	Optional<User> findBySsoId(String paycoIdNo);
}
