package com.nhnacademy.bookstoreback.user.domain.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.Check;

import com.nhnacademy.bookstoreback.user.domain.dto.request.CreateUserRequest;
import com.nhnacademy.bookstoreback.user.domain.dto.request.UpdateUserInfoRequest;
import com.nhnacademy.bookstoreback.usergrade.domain.entity.UserGrade;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Check(constraints = "user_role IN ('ADMIN', 'MEMBER') AND user_status IN ('ACTIVE', 'DORMANT', 'WITHDRAW')")
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

	@Enumerated(EnumType.STRING)
	@Column(name = "user_role")
	private Role role;

	@Enumerated(EnumType.STRING)
	@Column(name = "user_status")
	private UserStatus status;

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
		Long id,
		UserGrade userGrade,
		String name,
		String email,
		String password,
		LocalDate birth,
		String contact,
		BigDecimal points,
		Role role,
		UserStatus status,
		String ssoId,
		LocalDateTime createdAt,
		LocalDateTime updatedAt,
		LocalDateTime lastLoginAt
	) {
		this.id = id;
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

	public void update(UpdateUserInfoRequest updateUserInfoRequest) {
		this.name = updateUserInfoRequest.name();
		this.email = updateUserInfoRequest.email();
		this.birth = updateUserInfoRequest.birth();
		this.contact = updateUserInfoRequest.contact();
		this.updatedAt = LocalDateTime.now();
	}

	public static User toEntity(CreateUserRequest createUserRequest, String encodedPassword) {
		return User.builder()
			.name(createUserRequest.name())
			.email(createUserRequest.email())
			.password(encodedPassword)
			.birth(createUserRequest.birth())
			.contact(createUserRequest.contact())
			.points(BigDecimal.ZERO)
			.role(Role.MEMBER)
			.status(UserStatus.ACTIVE)
			.ssoId(null)
			.createdAt(LocalDateTime.now())
			.updatedAt(LocalDateTime.now())
			.lastLoginAt(null)
			.build();
	}
}
