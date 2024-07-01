package com.nhnacademy.bookstoreback.role.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nhnacademy.bookstoreback.role.domain.dto.request.CreateRoleRequest;
import com.nhnacademy.bookstoreback.role.domain.dto.response.CreateRoleResponse;
import com.nhnacademy.bookstoreback.role.domain.dto.response.GetRoleResponse;
import com.nhnacademy.bookstoreback.role.domain.entity.Role;
import com.nhnacademy.bookstoreback.role.exception.RoleAlreadyExistsException;
import com.nhnacademy.bookstoreback.role.repository.RoleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class RoleService {
	private final RoleRepository roleRepository;

	public CreateRoleResponse createRole(CreateRoleRequest createRoleRequest) {
		if (roleRepository.existsByRoleName(createRoleRequest.roleName())) {
			throw new RoleAlreadyExistsException(createRoleRequest.roleName());
		}

		Role role = Role.toEntity(createRoleRequest);
		Role savedRole = roleRepository.save(role);

		return CreateRoleResponse.fromEntity(savedRole);
	}

	public List<GetRoleResponse> getRoles() {
		List<Role> roles = roleRepository.findAll();

		return roles.stream()
			.map(GetRoleResponse::fromEntity)
			.toList();
	}

	public void deleteRole(String roleName) {
		roleRepository.deleteByRoleName(roleName);
	}
}
