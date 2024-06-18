package com.nhnacademy.bookstoreback.user.domain.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.nhnacademy.bookstoreback.usergrade.domain.entity.UserGrade;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "users")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "user_grade_id")
	private UserGrade userGrade;

	@Column(name = "user_name")
	private String name;

	@Column(name = "user_email")
	private String email;

	@Column(name = "user_password")
	private String password;

	@Column(name = "user_birth")
	private LocalDate birth;

	@Column(name = "user_contact")
	private String contact;

	@Column(name = "user_point")
	private BigDecimal points;

	@Column(name = "user_role")
	private Role role;

	@Column(name = "user_status")
	private String status;

	@Column(name = "user_sso_id")
	private String ssoId;

	@Column(name = "user_created_date")
	private LocalDateTime createdAt;

	@Column(name = "user_updated_date")
	private LocalDateTime updatedAt;

	@Column(name = "user_last_login_date")
	private LocalDateTime lastLoginAt;

	@Builder
	public User(
		UserGrade userGrade,
		String name,
		String email,
		String password,
		LocalDate birth,
		String contact,
		BigDecimal points,
		Role role,
		String status,
		String ssoId,
		LocalDateTime createdAt,
		LocalDateTime updatedAt,
		LocalDateTime lastLoginAt
	) {
		this.userGrade = userGrade;
		this.name = name;
		this.email = email;
		this.password = password;
		this.birth = birth;
		this.contact = contact;
		this.points = points;
		this.role = role;
		this.status = status;
		this.ssoId = ssoId;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.lastLoginAt = lastLoginAt;
	}
}
