package com.nhnacademy.bookstoreback.usergrade.domain.entity;

import java.math.BigDecimal;

import com.nhnacademy.bookstoreback.usergrade.domain.dto.request.CreateUserGradeRequest;
import com.nhnacademy.bookstoreback.usergrade.domain.dto.request.UpdateUserGradeRequest;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "user_grades")
public class UserGrade {
	@Id
	@Column(name = "user_grade_name")
	@NotNull
	private String userGradeName;

	@Column(name = "user_grade_min_amount")
	@NotNull
	private BigDecimal userGradeMinAmount;

	@Column(name = "user_grade_max_amount")
	private BigDecimal userGradeMaxAmount;

	@Column(name = "user_grade_point_rate")
	@NotNull
	private BigDecimal userGradePointRate;

	@Builder
	public UserGrade(String userGradeName,
		BigDecimal userGradeMinAmount,
		BigDecimal userGradeMaxAmount,
		BigDecimal userGradePointRate) {
		this.userGradeName = userGradeName;
		this.userGradeMinAmount = userGradeMinAmount;
		this.userGradeMaxAmount = userGradeMaxAmount;
		this.userGradePointRate = userGradePointRate;
	}

	public static UserGrade toEntity(CreateUserGradeRequest createUserGradeRequest) {
		return UserGrade.builder()
			.userGradeName(createUserGradeRequest.userGradeName())
			.userGradeMinAmount(createUserGradeRequest.userGradeMinAmount())
			.userGradeMaxAmount(createUserGradeRequest.userGradeMaxAmount())
			.userGradePointRate(createUserGradeRequest.userGradePointRate())
			.build();
	}

	public void update(UpdateUserGradeRequest updateUserGradeRequest) {
		this.userGradeMinAmount = updateUserGradeRequest.userGradeMinAmount();
		this.userGradeMaxAmount = updateUserGradeRequest.userGradeMaxAmount();
		this.userGradePointRate = updateUserGradeRequest.userGradePointRate();
	}
}
