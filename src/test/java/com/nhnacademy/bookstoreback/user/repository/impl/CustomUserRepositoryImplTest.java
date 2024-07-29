package com.nhnacademy.bookstoreback.user.repository.impl;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.nhnacademy.bookstoreback.user.domain.dto.response.BirthdayCouponTargetResponse;
import com.nhnacademy.bookstoreback.user.domain.entity.QUser;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;




class CustomUserRepositoryImplTest {

	@Mock
	private JPAQueryFactory queryFactory;

	@Mock
	private JPAQuery<BirthdayCouponTargetResponse> jpaQuery;

	@InjectMocks
	private CustomUserRepositoryImpl customUserRepository;

	private QUser qUser;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		qUser = QUser.user;
	}

	@Test
	void findUsersWithBirthMonthDay_ShouldReturnBirthdayCouponTargetResponses() {
		// Given
		int month = 7;
		int day = 14;

		// Create mock responses
		BirthdayCouponTargetResponse response1 = new BirthdayCouponTargetResponse(1L, LocalDate.of(1990, month, day));
		BirthdayCouponTargetResponse response2 = new BirthdayCouponTargetResponse(2L, LocalDate.of(1985, month, day));

		// Define the list of responses
		List<BirthdayCouponTargetResponse> responses = List.of(response1, response2);

		// Mock the JPAQueryFactory behavior
		when(queryFactory.select(any(Expression.class))).thenReturn(jpaQuery);
		when(jpaQuery.from(qUser)).thenReturn(jpaQuery);
		when(jpaQuery.where(any(BooleanExpression.class))).thenReturn(jpaQuery);
		when(jpaQuery.fetch()).thenReturn(responses);

		// When
		List<BirthdayCouponTargetResponse> results = customUserRepository.findUsersWithBirthMonthDay(month, day);

		// Then
		assertThat(results).isNotNull().hasSize(2);
		assertThat(results).containsExactlyInAnyOrder(
			new BirthdayCouponTargetResponse(1L, LocalDate.of(1990, month, day)),
			new BirthdayCouponTargetResponse(2L, LocalDate.of(1985, month, day))
		);
	}
}

