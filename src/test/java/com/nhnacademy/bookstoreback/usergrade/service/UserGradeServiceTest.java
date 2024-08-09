package com.nhnacademy.bookstoreback.usergrade.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.nhnacademy.bookstoreback.usergrade.domain.dto.request.CreateUserGradeRequest;
import com.nhnacademy.bookstoreback.usergrade.domain.dto.request.UpdateUserGradeRequest;
import com.nhnacademy.bookstoreback.usergrade.domain.dto.response.CreateUserGradeResponse;
import com.nhnacademy.bookstoreback.usergrade.domain.dto.response.GetUserGradeResponse;
import com.nhnacademy.bookstoreback.usergrade.domain.dto.response.UpdateUserGradeResponse;
import com.nhnacademy.bookstoreback.usergrade.domain.entity.UserGrade;
import com.nhnacademy.bookstoreback.usergrade.exception.UserGradeAlreadyExistsException;
import com.nhnacademy.bookstoreback.usergrade.exception.UserGradeNotFoundException;
import com.nhnacademy.bookstoreback.usergrade.repository.UserGradeRepository;

@ExtendWith(MockitoExtension.class)
class UserGradeServiceTest {

	@InjectMocks
	private UserGradeService userGradeService;

	@Mock
	private UserGradeRepository userGradeRepository;

	private CreateUserGradeRequest createUserGradeRequest;
	private UpdateUserGradeRequest updateUserGradeRequest;
	private UserGrade userGrade;

	@BeforeEach
	void setUp() {
		createUserGradeRequest = CreateUserGradeRequest.builder()
			.userGradeName("VIP")
			.userGradeMinAmount(BigDecimal.valueOf(1000))
			.userGradeMaxAmount(BigDecimal.valueOf(5000))
			.userGradePointRate(BigDecimal.valueOf(0.1))
			.build();

		updateUserGradeRequest = UpdateUserGradeRequest.builder()
			.userGradeMinAmount(BigDecimal.valueOf(2000))
			.userGradeMaxAmount(BigDecimal.valueOf(6000))
			.userGradePointRate(BigDecimal.valueOf(0.15))
			.build();

		userGrade = UserGrade.builder()
			.userGradeName("VIP")
			.userGradeMinAmount(BigDecimal.valueOf(1000))
			.userGradeMaxAmount(BigDecimal.valueOf(5000))
			.userGradePointRate(BigDecimal.valueOf(0.1))
			.build();
	}

	@Test
	void testCreateUserGrade() {
		// given
		given(userGradeRepository.existsByUserGradeName(createUserGradeRequest.userGradeName())).willReturn(false);
		given(userGradeRepository.save(any(UserGrade.class))).willReturn(userGrade);

		// when
		CreateUserGradeResponse response = userGradeService.createUserGrade(createUserGradeRequest);

		// then
		assertThat(response).isNotNull();
		assertThat(response.userGradeName()).isEqualTo(userGrade.getUserGradeName());
		verify(userGradeRepository).save(any(UserGrade.class));
	}

	@Test
	void testCreateUserGradeAlreadyExists() {
		// given
		given(userGradeRepository.existsByUserGradeName(createUserGradeRequest.userGradeName())).willReturn(true);

		// when / then
		assertThrows(UserGradeAlreadyExistsException.class,
			() -> userGradeService.createUserGrade(createUserGradeRequest));
	}

	@Test
	void testGetUserGrades() {
		// given
		given(userGradeRepository.findAll()).willReturn(Collections.singletonList(userGrade));

		// when
		List<GetUserGradeResponse> responses = userGradeService.getUserGrades();

		// then
		assertThat(responses).hasSize(1);
		assertThat(responses.get(0).userGradeName()).isEqualTo(userGrade.getUserGradeName());
	}

	@Test
	void testGetUserGrade() {
		// given
		given(userGradeRepository.findByUserGradeName(userGrade.getUserGradeName())).willReturn(Optional.of(userGrade));

		// when
		GetUserGradeResponse response = userGradeService.getUserGrade(userGrade.getUserGradeName());

		// then
		assertThat(response).isNotNull();
		assertThat(response.userGradeName()).isEqualTo(userGrade.getUserGradeName());
	}

	@Test
	void testGetUserGradeNotFound() {
		// given
		given(userGradeRepository.findByUserGradeName(userGrade.getUserGradeName())).willReturn(Optional.empty());

		// when / then
		assertThrows(UserGradeNotFoundException.class,
			() -> userGradeService.getUserGrade(userGrade.getUserGradeName()));
	}

	@Test
	void testUpdateUserGrade() {
		// given
		given(userGradeRepository.findByUserGradeName(userGrade.getUserGradeName())).willReturn(Optional.of(userGrade));
		given(userGradeRepository.save(any(UserGrade.class))).willReturn(userGrade);

		// when
		UpdateUserGradeResponse response = userGradeService.updateUserGrade(userGrade.getUserGradeName(),
			updateUserGradeRequest);

		// then
		assertThat(response).isNotNull();
		assertThat(response.userGradeName()).isEqualTo(userGrade.getUserGradeName());
		verify(userGradeRepository).save(any(UserGrade.class));
	}

	@Test
	void testUpdateUserGradeNotFound() {
		// given
		given(userGradeRepository.findByUserGradeName(userGrade.getUserGradeName())).willReturn(Optional.empty());

		// when / then
		assertThrows(UserGradeNotFoundException.class,
			() -> userGradeService.updateUserGrade(userGrade.getUserGradeName(), updateUserGradeRequest));
	}

	@Test
	void testDeleteUserGrade() {
		// given
		given(userGradeRepository.findByUserGradeName(userGrade.getUserGradeName())).willReturn(Optional.of(userGrade));

		// when
		userGradeService.deleteUserGrade(userGrade.getUserGradeName());

		// then
		verify(userGradeRepository).delete(userGrade);
	}

	@Test
	void testDeleteUserGradeNotFound() {
		// given
		given(userGradeRepository.findByUserGradeName(userGrade.getUserGradeName())).willReturn(Optional.empty());

		// when / then
		assertThrows(UserGradeNotFoundException.class,
			() -> userGradeService.deleteUserGrade(userGrade.getUserGradeName()));
	}
}
