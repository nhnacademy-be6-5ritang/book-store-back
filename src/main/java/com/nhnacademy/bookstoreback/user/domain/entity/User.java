package com.nhnacademy.bookstoreback.user.domain.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.nhnacademy.bookstoreback.address.domain.entity.Address;
import com.nhnacademy.bookstoreback.user.domain.dto.request.CreateUserRequest;
import com.nhnacademy.bookstoreback.user.domain.dto.request.UpdateUserInfoRequest;
import com.nhnacademy.bookstoreback.usergrade.domain.entity.UserGrade;
import com.nhnacademy.bookstoreback.userrole.domain.entity.UserRole;
import com.nhnacademy.bookstoreback.userstatus.domain.entity.UserStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "users", indexes = @Index(name = "idx_user_email", columnList = "user_email", unique = true))
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private Long id;

	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "user_grade_name")
	private UserGrade userGrade;

	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "user_status_id")
	private UserStatus status;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<UserRole> userRoles = new ArrayList<>();

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<Address> addresses = new ArrayList<>();

	@Column(name = "user_name")
	@NotBlank
	@Size(max = 10)
	private String name;

	@Column(name = "user_email")
	@NotBlank
	@Size(max = 30)
	private String email;

	@Column(name = "user_password")
	@NotBlank
	@Size(min = 8, max = 100)
	private String password;

	@Column(name = "user_birth")
	// @NotNull
	private LocalDate birth;

	@Column(name = "user_contact")
	@NotBlank
	@Size(min = 11, max = 11)
	private String contact;

	@Column(name = "user_point")
	@NotNull
	private BigDecimal points;

	@Column(name = "user_sso_id")
	private String ssoId;

	@Column(name = "user_created_date")
	@NotNull
	private LocalDateTime createdAt;

	@Column(name = "user_updated_date")
	@NotNull
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
		this.userRoles = userRoles != null ? userRoles : new ArrayList<>();
		this.addresses = addresses != null ? addresses : new ArrayList<>();
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

	public static User toEntity(CreateUserRequest createUserRequest, String encodedPassword) {
		return User.builder()
			.name(createUserRequest.name())
			.email(createUserRequest.email())
			.password(encodedPassword)
			.birth(createUserRequest.getBirthDate())
			.contact(createUserRequest.contact())
			.points(BigDecimal.ZERO)
			.createdAt(LocalDateTime.now())
			.updatedAt(LocalDateTime.now())
			.lastLoginAt(LocalDateTime.now())
			.build();
	}

	public void update(UpdateUserInfoRequest updateUserInfoRequest) {
		this.name = updateUserInfoRequest.name();
		this.birth = updateUserInfoRequest.birth();
		this.contact = updateUserInfoRequest.contact();
		this.updatedAt = LocalDateTime.now();
		this.password = updateUserInfoRequest.password();
	}

	public void updateNotPassword(UpdateUserInfoRequest updateUserInfoRequest) {
		this.name = updateUserInfoRequest.name();
		this.birth = updateUserInfoRequest.birth();
		this.contact = updateUserInfoRequest.contact();
		this.updatedAt = LocalDateTime.now();
	}

	public List<String> getAllRoles() {
		return userRoles.stream()
			.map(UserRole::getRoleName)
			.collect(Collectors.toList());
	}

	public void updateUserStatus(UserStatus status) {
		this.status = status;
	}

	public void updateUserGrade(UserGrade userGrade) {
		this.userGrade = userGrade;
	}

	public void updatePoints(BigDecimal incomingPoints) {
		this.points = this.points.add(incomingPoints);
	}

	public void updateOutPoints(BigDecimal outPoints) {
		this.points = this.points.subtract(outPoints);
	}

	public void updateLastLoginAt(LocalDateTime lastLoginAt) {
		this.lastLoginAt = lastLoginAt;
	}

	public void updateSsoId(String memberId) {
		this.ssoId = memberId;
	}
}
