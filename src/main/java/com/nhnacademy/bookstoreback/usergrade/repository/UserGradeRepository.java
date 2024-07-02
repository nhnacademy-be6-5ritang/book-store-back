package com.nhnacademy.bookstoreback.usergrade.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nhnacademy.bookstoreback.usergrade.domain.entity.UserGrade;

public interface UserGradeRepository extends JpaRepository<UserGrade, String> {
	boolean existsByUserGradeName(String userGradeName);

	Optional<UserGrade> findByUserGradeName(String userGradeName);
}
