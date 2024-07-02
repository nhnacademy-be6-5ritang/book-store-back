package com.nhnacademy.bookstoreback.userstatus.domain.entity;

import com.nhnacademy.bookstoreback.userstatus.domain.dto.request.CreateUserStatusRequest;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "users_statuses")
public class UserStatus {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_status_id")
	private Long id;

	@Column(name = "user_status_name")
	private String userStatusName;

	@Builder
	public UserStatus(String userStatusName) {
		this.userStatusName = userStatusName;
	}

	public static UserStatus toEntity(CreateUserStatusRequest createUserStatusRequest) {
		return UserStatus.builder()
			.userStatusName(createUserStatusRequest.userStatusName())
			.build();
	}
}
