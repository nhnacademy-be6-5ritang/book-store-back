package com.nhnacademy.bookstoreback.usergrade.response;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.nhnacademy.bookstoreback.usergrade.domain.dto.response.CreateUserGradeResponse;
import com.nhnacademy.bookstoreback.usergrade.domain.dto.response.GetUserGradeResponse;
import com.nhnacademy.bookstoreback.usergrade.domain.dto.response.UpdateUserGradeResponse;
import com.nhnacademy.bookstoreback.usergrade.domain.entity.UserGrade;

class UserGradeResponseTest {

	private UserGrade userGrade;

	@BeforeEach
	void setUp() {
		userGrade = UserGrade.builder()
			.userGradeName("VIP")
			.userGradeMinAmount(BigDecimal.valueOf(1000))
			.userGradeMaxAmount(BigDecimal.valueOf(5000))
			.userGradePointRate(BigDecimal.valueOf(0.1))
			.build();
	}

	@Test
	void testCreateUserGradeResponseFromEntity() {
		// when
		CreateUserGradeResponse response = CreateUserGradeResponse.fromEntity(userGrade);

		// then
		assertThat(response).isNotNull();
		assertThat(response.userGradeName()).isEqualTo(userGrade.getUserGradeName());
		assertThat(response.userGradeMinAmount()).isEqualTo(userGrade.getUserGradeMinAmount());
		assertThat(response.userGradeMaxAmount()).isEqualTo(userGrade.getUserGradeMaxAmount());
		assertThat(response.userGradePointRate()).isEqualTo(userGrade.getUserGradePointRate());
	}

	@Test
	void testGetUserGradeResponseFromEntity() {
		// when
		GetUserGradeResponse response = GetUserGradeResponse.fromEntity(userGrade);

		// then
		assertThat(response).isNotNull();
		assertThat(response.userGradeName()).isEqualTo(userGrade.getUserGradeName());
		assertThat(response.userGradeMinAmount()).isEqualTo(userGrade.getUserGradeMinAmount());
		assertThat(response.userGradeMaxAmount()).isEqualTo(userGrade.getUserGradeMaxAmount());
		assertThat(response.userGradePointRate()).isEqualTo(userGrade.getUserGradePointRate());
	}

	@Test
	void testUpdateUserGradeResponseFromEntity() {
		// when
		UpdateUserGradeResponse response = UpdateUserGradeResponse.fromEntity(userGrade);

		// then
		assertThat(response).isNotNull();
		assertThat(response.userGradeName()).isEqualTo(userGrade.getUserGradeName());
		assertThat(response.userGradeMinAmount()).isEqualTo(userGrade.getUserGradeMinAmount());
		assertThat(response.userGradeMaxAmount()).isEqualTo(userGrade.getUserGradeMaxAmount());
		assertThat(response.userGradePointRate()).isEqualTo(userGrade.getUserGradePointRate());
	}
}
