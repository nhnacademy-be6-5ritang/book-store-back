package com.nhnacademy.bookstoreback.user.repository.impl;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import com.nhnacademy.bookstoreback.address.domain.entity.Address;
import com.nhnacademy.bookstoreback.config.QuerydslTestConfig;
import com.nhnacademy.bookstoreback.user.domain.dto.response.BirthdayCouponTargetResponse;
import com.nhnacademy.bookstoreback.user.domain.entity.User;
import com.nhnacademy.bookstoreback.user.repository.CustomUserRepository;
import com.nhnacademy.bookstoreback.usergrade.domain.entity.UserGrade;
import com.nhnacademy.bookstoreback.userstatus.domain.entity.UserStatus;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;

@DataJpaTest
@Import(QuerydslTestConfig.class) // QueryDSL 설정 파일 임포트
class CustomUserRepositoryImplTest {

	@Autowired
	private EntityManager entityManager;

	@Autowired
	private JPAQueryFactory jpaQueryFactory;

	private CustomUserRepository customUserRepository;

	@BeforeEach
	void setUp() {
		customUserRepository = new CustomUserRepositoryImpl(entityManager);

		// Create UserGrade
		UserGrade userGrade = UserGrade.builder()
			.userGradeName("Silver")
			.userGradeMinAmount(BigDecimal.valueOf(1000))
			.userGradeMaxAmount(BigDecimal.valueOf(5000))
			.userGradePointRate(BigDecimal.valueOf(0.05))
			.build();
		entityManager.persist(userGrade);

		// Create UserStatus
		UserStatus userStatus = UserStatus.builder()
			.userStatusName("Active")
			.build();
		entityManager.persist(userStatus);

		// Create Users
		User user1 = User.builder()
			.name("이기훈")
			.email("kihoon@naver.com")
			.password("1234567890")
			.birth(LocalDate.of(1990, 7, 29)) // July 29
			.contact("01012345678")
			.points(BigDecimal.valueOf(100))
			.ssoId("sso-1234")
			.createdAt(LocalDateTime.now())
			.updatedAt(LocalDateTime.now())
			.build();
		entityManager.persist(user1);

		User user2 = User.builder()
			.name("김제니")
			.email("jenny@example.com")
			.password("1234567890")
			.birth(LocalDate.of(1991, 7, 29)) // July 29
			.contact("01098765432")
			.points(BigDecimal.valueOf(200))
			.ssoId("sso-5678")
			.createdAt(LocalDateTime.now())
			.updatedAt(LocalDateTime.now())
			.build();
		entityManager.persist(user2);

		User user3 = User.builder()
			.name("김나영")
			.email("nayoung@example.com")
			.password("1234567890")
			.birth(LocalDate.of(1992, 8, 15)) // August 15
			.contact("01011112222")
			.points(BigDecimal.valueOf(300))
			.ssoId("sso-91011")
			.createdAt(LocalDateTime.now())
			.updatedAt(LocalDateTime.now())
			.build();
		entityManager.persist(user3);

		// Create Address
		Address address = Address.builder()
			.user(user1)
			.postCode("12345")
			.base("Base Address")
			.detail("Detail Address")
			.alias("Home")
			.isDefault(true)
			.build();
		entityManager.persist(address);

		// Flush and clear to ensure persistence context is updated
		entityManager.flush();
		entityManager.clear();
	}


	@AfterEach
	void resetAutoIncrementId() {
		entityManager.createNativeQuery("ALTER TABLE users ALTER COLUMN user_id RESTART WITH 1")
			.executeUpdate();
	}

	@Test
	void testFindUsersWithMatchingBirthDate() {
		// Given
		int month = 7; // July
		int day = 29;

		// When
		List<BirthdayCouponTargetResponse> responses = customUserRepository.findUsersWithBirthMonthDay(month, day);

		// Then
		assertThat(responses).hasSize(2); // Expecting two users with the birth date July 29
		assertThat(responses).extracting("userId").containsExactlyInAnyOrder(1L, 2L);
	}

	@Test
	void testFindUsersWithNonMatchingBirthDate() {
		// Given
		int month = 12; // December
		int day = 31;

		// When
		List<BirthdayCouponTargetResponse> responses = customUserRepository.findUsersWithBirthMonthDay(month, day);

		// Then
		assertThat(responses).isEmpty(); // No users with the birth date December 31
	}

	@Test
	void testFindUsersWithMultipleBirthDates() {
		// Given
		int month = 8; // August
		int day = 15;

		// When
		List<BirthdayCouponTargetResponse> responses = customUserRepository.findUsersWithBirthMonthDay(month, day);

		// Then
		assertThat(responses).hasSize(1); // Expecting one user with the birth date August 15
		assertThat(responses).extracting("userId").containsExactly(3L);
	}

	@Test
	void testFindUsersWithEmptyDatabase() {
		// Clear Address entities first
		entityManager.createQuery("DELETE FROM Address").executeUpdate();
		entityManager.flush();

		// Clear User entities
		entityManager.createQuery("DELETE FROM User").executeUpdate();
		entityManager.flush();
		entityManager.clear();

		// Given
		int month = 7; // July
		int day = 29;

		// When
		List<BirthdayCouponTargetResponse> responses = customUserRepository.findUsersWithBirthMonthDay(month, day);

		// Then
		assertThat(responses).isEmpty(); // No users in the database
	}
}