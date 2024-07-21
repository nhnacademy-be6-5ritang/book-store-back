package com.nhnacademy.bookstoreback.role.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.nhnacademy.bookstoreback.role.domain.dto.request.CreateRoleRequest;
import com.nhnacademy.bookstoreback.role.domain.dto.response.CreateRoleResponse;
import com.nhnacademy.bookstoreback.role.domain.dto.response.GetRoleResponse;
import com.nhnacademy.bookstoreback.role.domain.entity.Role;
import com.nhnacademy.bookstoreback.role.exception.RoleAlreadyExistsException;
import com.nhnacademy.bookstoreback.role.repository.RoleRepository;

class RoleServiceTest {

	@InjectMocks
	private RoleService roleService;

	@Mock
	private RoleRepository roleRepository;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testCreateRole() {
		CreateRoleRequest request = CreateRoleRequest.builder()
			.roleName("ADMIN")
			.build();
		Role role = Role.builder()
			.roleName("ADMIN")
			.build();
		Role savedRole = Role.builder()
			.roleName("ADMIN")
			.build();
		CreateRoleResponse response = CreateRoleResponse.builder()
			.roleName("ADMIN")
			.build();

		when(roleRepository.existsByRoleName(anyString())).thenReturn(false);
		when(roleRepository.save(any(Role.class))).thenReturn(savedRole);

		CreateRoleResponse result = roleService.createRole(request);

		assertNotNull(result);
		assertEquals("ADMIN", result.roleName());
		verify(roleRepository).existsByRoleName("ADMIN");
		verify(roleRepository).save(any(Role.class));
	}

	@Test
	void testCreateRoleAlreadyExists() {
		CreateRoleRequest request = CreateRoleRequest.builder()
			.roleName("ADMIN")
			.build();

		when(roleRepository.existsByRoleName(anyString())).thenReturn(true);

		assertThrows(RoleAlreadyExistsException.class, () -> {
			roleService.createRole(request);
		});

		verify(roleRepository).existsByRoleName("ADMIN");
		verify(roleRepository, never()).save(any(Role.class));
	}

	@Test
	void testGetRoles() {
		Role role1 = Role.builder()
			.roleName("ADMIN")
			.build();
		Role role2 = Role.builder()
			.roleName("USER")
			.build();

		List<Role> roles = Arrays.asList(role1, role2);

		when(roleRepository.findAll()).thenReturn(roles);

		List<GetRoleResponse> result = roleService.getRoles();

		assertNotNull(result);
		assertEquals(2, result.size());
		assertEquals("ADMIN", result.get(0).roleName());
		assertEquals("USER", result.get(1).roleName());
		verify(roleRepository).findAll();
	}

	@Test
	void testDeleteRole() {
		String roleName = "ADMIN";

		doNothing().when(roleRepository).deleteByRoleName(anyString());

		roleService.deleteRole(roleName);

		verify(roleRepository).deleteByRoleName(roleName);
	}
}
