package com.nhnacademy.bookstoreback.role.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.bookstoreback.role.domain.dto.request.CreateRoleRequest;
import com.nhnacademy.bookstoreback.role.domain.dto.response.CreateRoleResponse;
import com.nhnacademy.bookstoreback.role.domain.dto.response.GetRoleResponse;
import com.nhnacademy.bookstoreback.role.service.RoleService;

@WebMvcTest(RoleController.class)
public class RoleControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private RoleService roleService;

	private ObjectMapper objectMapper = new ObjectMapper();

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	@WithMockUser(roles = "MEMBER_ADMIN")
	void testCreateRole() throws Exception {
		CreateRoleRequest request = CreateRoleRequest.builder()
			.roleName("ADMIN")
			.build();
		CreateRoleResponse response = CreateRoleResponse.builder()
			.roleName("ADMIN")
			.build();

		when(roleService.createRole(any(CreateRoleRequest.class))).thenReturn(response);

		mockMvc.perform(post("/roles")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))
				.with(csrf())
			)
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.roleName").value("ADMIN"));
	}

	@Test
	@WithMockUser(roles = "MEMBER_ADMIN")
	void testGetRoles() throws Exception {
		List<GetRoleResponse> responses = Arrays.asList(
			GetRoleResponse.builder()
				.roleName("TEST_MEMBER")
				.build(),
			GetRoleResponse.builder()
				.roleName("TEST_MEMBER_ADMIN")
				.build()
		);

		when(roleService.getRoles()).thenReturn(responses);

		mockMvc.perform(get("/roles"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$[0].roleName").value("TEST_MEMBER"))
			.andExpect(jsonPath("$[1].roleName").value("TEST_MEMBER_ADMIN"));
	}

	@Test
	@WithMockUser(roles = "MEMBER_ADMIN")
	void testDeleteRole() throws Exception {
		String roleName = "ADMIN";

		doNothing().when(roleService).deleteRole(roleName);

		mockMvc.perform(delete("/roles/{roleName}", roleName)
				.with(csrf())
			)
			.andExpect(status().isNoContent());
	}
}
