package com.nhnacademy.bookstoreback.role.domain.entity;

import com.nhnacademy.bookstoreback.role.domain.dto.request.CreateRoleRequest;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "roles")
public class Role {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "role_id")
	private Long id;

	@Column(name = "role_name")
	@NotNull
	private String roleName;

	@Builder
	public Role(Long id, String roleName) {
		this.id = id;
		this.roleName = roleName;
	}

	public static Role toEntity(CreateRoleRequest createRoleRequest) {
		return Role.builder()
			.roleName(createRoleRequest.roleName())
			.build();
	}
}
