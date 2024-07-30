package com.nhnacademy.bookstoreback.userrole.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nhnacademy.bookstoreback.role.domain.entity.Role;
import com.nhnacademy.bookstoreback.role.exception.RoleNotFoundException;
import com.nhnacademy.bookstoreback.role.repository.RoleRepository;
import com.nhnacademy.bookstoreback.user.domain.entity.User;
import com.nhnacademy.bookstoreback.user.exception.UserNotFoundException;
import com.nhnacademy.bookstoreback.user.repository.UserRepository;
import com.nhnacademy.bookstoreback.userrole.domain.dto.response.AddUserRoleResponse;
import com.nhnacademy.bookstoreback.userrole.domain.dto.response.GetUserRoleResponse;
import com.nhnacademy.bookstoreback.userrole.domain.entity.UserRole;
import com.nhnacademy.bookstoreback.userrole.exception.UserHasRoleAlreadyException;
import com.nhnacademy.bookstoreback.userrole.repository.UserRoleRepository;

import lombok.RequiredArgsConstructor;

/**
 * @author 김태환
 * 사용자 역할 관련 서비스를 제공하는 인터페이스입니다.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class UserRoleService {
	private final UserRoleRepository userRoleRepository;
	private final UserRepository userRepository;
	private final RoleRepository roleRepository;

	/**
	 * 사용자의 역할을 추가합니다.
	 *
	 * @param userId 역할을 추가할 사용자 ID
	 * @param roleId 추가할 역할 ID
	 * @return 추가된 사용자 역할 응답 DTO
	 */
	public AddUserRoleResponse addUserRole(Long userId, Long roleId) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new UserNotFoundException(userId));

		Role role = roleRepository.findById(roleId)
			.orElseThrow(() -> new RoleNotFoundException(roleId));

		UserRole userRole = UserRole.builder()
			.user(user)
			.role(role)
			.build();

		if (userRoleRepository.existsByUserAndRole(user, role)) {
			throw new UserHasRoleAlreadyException(userId, role.getRoleName());
		}

		UserRole savedUserRole = userRoleRepository.save(userRole);

		return AddUserRoleResponse.fromEntity(savedUserRole);
	}

	/**
	 * 특정 사용자의 역할을 조회합니다.
	 *
	 * @param userId 조회할 사용자 ID
	 * @return 사용자 역할 응답 DTO
	 */
	public GetUserRoleResponse getUserRoles(Long userId) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new UserNotFoundException(userId));

		List<UserRole> userRoles = userRoleRepository.findByUser(user);

		return GetUserRoleResponse.fromEntities(userId, userRoles);
	}

	/**
	 * 특정 사용자의 역할을 삭제합니다.
	 *
	 * @param userId 역할을 삭제할 사용자 ID
	 * @param roleId 삭제할 역할 ID
	 */
	public void deleteUserRole(Long userId, Long roleId) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new UserNotFoundException(userId));

		Role role = roleRepository.findById(roleId)
			.orElseThrow(() -> new RoleNotFoundException(roleId));

		userRoleRepository.deleteByUserAndRole(user, role);
	}
}
