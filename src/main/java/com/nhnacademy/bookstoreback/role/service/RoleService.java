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

/**
 * @author 김태환
 * 역할 관리와 관련된 서비스 인터페이스입니다.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class RoleService {
	private final RoleRepository roleRepository;

	/**
	 * 새로운 역할을 생성합니다.
	 *
	 * @param createRoleRequest 생성할 역할 정보
	 * @return 생성된 역할의 정보
	 * @throws RoleAlreadyExistsException 역할 이름이 이미 존재하는 경우 발생
	 */
	public CreateRoleResponse createRole(CreateRoleRequest createRoleRequest) {
		if (roleRepository.existsByRoleName(createRoleRequest.roleName())) {
			throw new RoleAlreadyExistsException(createRoleRequest.roleName());
		}

		Role role = Role.toEntity(createRoleRequest);
		Role savedRole = roleRepository.save(role);

		return CreateRoleResponse.fromEntity(savedRole);
	}

	/**
	 * 모든 역할을 조회합니다.
	 *
	 * @return 모든 역할의 목록
	 */
	public List<GetRoleResponse> getRoles() {
		List<Role> roles = roleRepository.findAll();

		return roles.stream()
			.map(GetRoleResponse::fromEntity)
			.toList();
	}

	/**
	 * 주어진 역할 이름으로 역할을 삭제합니다.
	 *
	 * @param roleName 삭제할 역할의 이름
	 */
	public void deleteRole(String roleName) {
		roleRepository.deleteByRoleName(roleName);
	}
}
