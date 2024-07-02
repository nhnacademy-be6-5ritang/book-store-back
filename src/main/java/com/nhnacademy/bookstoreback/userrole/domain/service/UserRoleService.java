package com.nhnacademy.bookstoreback.userrole.domain.service;

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
import com.nhnacademy.bookstoreback.userrole.domain.repository.UserRoleRepository;
import com.nhnacademy.bookstoreback.userrole.exception.UserHasRoleAlreadyException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class UserRoleService {
	private final UserRoleRepository userRoleRepository;
	private final UserRepository userRepository;
	private final RoleRepository roleRepository;

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

	public GetUserRoleResponse getUserRoles(Long userId) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new UserNotFoundException(userId));

		List<UserRole> userRoles = userRoleRepository.findByUser(user);

		return GetUserRoleResponse.fromEntities(userId, userRoles);
	}

	public void deleteUserRole(Long userId, Long roleId) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new UserNotFoundException(userId));

		Role role = roleRepository.findById(roleId)
			.orElseThrow(() -> new RoleNotFoundException(roleId));

		userRoleRepository.deleteByUserAndRole(user, role);
	}
}
