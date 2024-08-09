package com.nhnacademy.bookstoreback.userrole.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

@ExtendWith(MockitoExtension.class)
class UserRoleServiceTest {

	@Mock
	private UserRoleRepository userRoleRepository;

	@Mock
	private UserRepository userRepository;

	@Mock
	private RoleRepository roleRepository;

	@InjectMocks
	private UserRoleService userRoleService;

	private User user;
	private Role role;
	private UserRole userRole;

	@BeforeEach
	void setUp() {
		user = User.builder().id(1L).name("Test User").build();
		role = Role.builder().id(1L).roleName("ROLE_USER").build();
		userRole = UserRole.builder().user(user).role(role).build();
	}

	@Test
	void testAddUserRole_Success() {
		when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
		when(roleRepository.findById(role.getId())).thenReturn(Optional.of(role));
		when(userRoleRepository.existsByUserAndRole(user, role)).thenReturn(false);
		when(userRoleRepository.save(any(UserRole.class))).thenReturn(userRole);

		AddUserRoleResponse response = userRoleService.addUserRole(user.getId(), role.getId());

		assertNotNull(response);
		assertEquals(user.getId(), response.userId());
		assertEquals(role.getId(), response.roleId());

		verify(userRepository).findById(user.getId());
		verify(roleRepository).findById(role.getId());
		verify(userRoleRepository).existsByUserAndRole(user, role);
		verify(userRoleRepository).save(any(UserRole.class));
	}

	@Test
	void testAddUserRole_UserNotFound() {
		when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

		assertThrows(UserNotFoundException.class, () -> userRoleService.addUserRole(user.getId(), role.getId()));

		verify(userRepository).findById(user.getId());
		verify(roleRepository, never()).findById(role.getId());
		verify(userRoleRepository, never()).existsByUserAndRole(any(User.class), any(Role.class));
		verify(userRoleRepository, never()).save(any(UserRole.class));
	}

	@Test
	void testAddUserRole_RoleNotFound() {
		when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
		when(roleRepository.findById(role.getId())).thenReturn(Optional.empty());

		assertThrows(RoleNotFoundException.class, () -> userRoleService.addUserRole(user.getId(), role.getId()));

		verify(userRepository).findById(user.getId());
		verify(roleRepository).findById(role.getId());
		verify(userRoleRepository, never()).existsByUserAndRole(any(User.class), any(Role.class));
		verify(userRoleRepository, never()).save(any(UserRole.class));
	}

	@Test
	void testAddUserRole_UserHasRoleAlready() {
		when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
		when(roleRepository.findById(role.getId())).thenReturn(Optional.of(role));
		when(userRoleRepository.existsByUserAndRole(user, role)).thenReturn(true);

		assertThrows(UserHasRoleAlreadyException.class, () -> userRoleService.addUserRole(user.getId(), role.getId()));

		verify(userRepository).findById(user.getId());
		verify(roleRepository).findById(role.getId());
		verify(userRoleRepository).existsByUserAndRole(user, role);
		verify(userRoleRepository, never()).save(any(UserRole.class));
	}

	@Test
	void testGetUserRoles_Success() {
		when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
		when(userRoleRepository.findByUser(user)).thenReturn(List.of(userRole));

		GetUserRoleResponse response = userRoleService.getUserRoles(user.getId());

		assertNotNull(response);
		assertEquals(user.getId(), response.userId());
		assertTrue(response.roleNames().contains(role.getRoleName()));
		assertTrue(response.roleIds().contains(role.getId()));

		verify(userRepository).findById(user.getId());
		verify(userRoleRepository).findByUser(user);
	}

	@Test
	void testGetUserRoles_UserNotFound() {
		when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

		assertThrows(UserNotFoundException.class, () -> userRoleService.getUserRoles(user.getId()));

		verify(userRepository).findById(user.getId());
		verify(userRoleRepository, never()).findByUser(any(User.class));
	}

	@Test
	void testDeleteUserRole_Success() {
		when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
		when(roleRepository.findById(role.getId())).thenReturn(Optional.of(role));

		userRoleService.deleteUserRole(user.getId(), role.getId());

		verify(userRepository).findById(user.getId());
		verify(roleRepository).findById(role.getId());
		verify(userRoleRepository).deleteByUserAndRole(user, role);
	}

	@Test
	void testDeleteUserRole_UserNotFound() {
		when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

		assertThrows(UserNotFoundException.class, () -> userRoleService.deleteUserRole(user.getId(), role.getId()));

		verify(userRepository).findById(user.getId());
		verify(roleRepository, never()).findById(role.getId());
		verify(userRoleRepository, never()).deleteByUserAndRole(any(User.class), any(Role.class));
	}

	@Test
	void testDeleteUserRole_RoleNotFound() {
		when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
		when(roleRepository.findById(role.getId())).thenReturn(Optional.empty());

		assertThrows(RoleNotFoundException.class, () -> userRoleService.deleteUserRole(user.getId(), role.getId()));

		verify(userRepository).findById(user.getId());
		verify(roleRepository).findById(role.getId());
		verify(userRoleRepository, never()).deleteByUserAndRole(any(User.class), any(Role.class));
	}
}