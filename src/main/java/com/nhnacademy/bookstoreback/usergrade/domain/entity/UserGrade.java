package com.nhnacademy.bookstoreback.usergrade.domain.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "user_grades")
public class UserGrade {
	@Id
	@Column(name = "user_grade_name")
	private String userGradeName;

	@Column(name = "user_grade_min_amount")
	private BigDecimal userGradeMinAmount;

	@Column(name = "user_grade_max_amount")
	private BigDecimal userGradeMaxAmount;

	@Column(name = "user_grade_point_rate")
	private BigDecimal userGradePointRate;
}
