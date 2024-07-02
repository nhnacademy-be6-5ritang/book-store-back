package com.nhnacademy.bookstoreback.user.domain.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.nhnacademy.bookstoreback.address.domain.entity.Address;
import com.nhnacademy.bookstoreback.user.domain.dto.request.CreateUserRequest;
import com.nhnacademy.bookstoreback.user.domain.dto.request.UpdateUserInfoRequest;
import com.nhnacademy.bookstoreback.usergrade.domain.entity.UserGrade;
import com.nhnacademy.bookstoreback.userrole.domain.entity.UserRole;
import com.nhnacademy.bookstoreback.userstatus.domain.entity.UserStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
	@JoinColumn(name = "user_grade_name")
	private UserGrade userGrade;

	@ManyToOne
	@JoinColumn(name = "user_status_name")
	private UserStatus status;

	@OneToMany(mappedBy = "user")
	private List<UserRole> userRoles;

	@OneToMany(mappedBy = "user")
	private List<Address> addresses;

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
		UserStatus status,
		List<UserRole> userRoles,
		List<Address> addresses,
		String name,
		String email,
		String password,
		LocalDate birth,
		String contact,
		BigDecimal points,
		String ssoId,
		LocalDateTime createdAt,
		LocalDateTime updatedAt,
		LocalDateTime lastLoginAt
	) {
		this.id = id;
		this.userGrade = userGrade;
		this.status = status;
		this.userRoles = userRoles;
		this.addresses = addresses;
		this.name = name;
		this.email = email;
		this.password = password;
		this.birth = birth;
		this.contact = contact;
		this.points = points;
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
			.createdAt(LocalDateTime.now())
			.updatedAt(LocalDateTime.now())
			.lastLoginAt(LocalDateTime.now())
			.build();
	}

	// role의 name만 가져오기
	public List<String> getAllRoles() {
		return userRoles.stream()
			.map(UserRole::getRoleName)
			.collect(Collectors.toList());
	}
}
