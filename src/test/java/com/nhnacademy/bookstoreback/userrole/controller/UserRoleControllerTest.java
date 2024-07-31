package com.nhnacademy.bookstoreback.userrole.controller;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.bookstoreback.userrole.domain.dto.response.AddUserRoleResponse;
import com.nhnacademy.bookstoreback.userrole.domain.dto.response.GetUserRoleResponse;
import com.nhnacademy.bookstoreback.userrole.service.UserRoleService;

@ExtendWith(MockitoExtension.class)
class UserRoleControllerTest {

	@InjectMocks
	private UserRoleController userRoleController;

	@Mock
	private UserRoleService userRoleService;

	private MockMvc mockMvc;
	private ObjectMapper objectMapper;

	private AddUserRoleResponse addUserRoleResponse;
	private GetUserRoleResponse getUserRoleResponse;

	@BeforeEach
	void setUp() {
		objectMapper = new ObjectMapper();
		mockMvc = MockMvcBuilders.standaloneSetup(userRoleController).build();
		addUserRoleResponse = AddUserRoleResponse.builder()
			.userId(1L)
			.roleId(1L)
			.roleNames(Collections.singletonList("ROLE_USER"))
			.build();
		getUserRoleResponse = GetUserRoleResponse.builder()
			.userId(1L)
			.roleIds(Collections.singletonList(1L))
			.roleNames(Collections.singletonList("ROLE_USER"))
			.build();
	}

	@Test
	void testAddUserRole() throws Exception {
		given(userRoleService.addUserRole(1L, 1L)).willReturn(addUserRoleResponse);

		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/user-roles/user/1/role/1")
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isCreated())
			.andReturn();

		String expectedJson = objectMapper.writeValueAsString(addUserRoleResponse);
		MockMvcResultMatchers.content().json(expectedJson).match(result);
	}

	@Test
	void testGetUserRoles() throws Exception {
		given(userRoleService.getUserRoles(1L)).willReturn(getUserRoleResponse);

		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/user-roles/user/1")
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andReturn();

		String expectedJson = objectMapper.writeValueAsString(getUserRoleResponse);
		MockMvcResultMatchers.content().json(expectedJson).match(result);
	}

	@Test
	void testRemoveUserRole() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.delete("/api/user-roles/user/1/role/1"))
			.andExpect(status().isNoContent());

		then(userRoleService).should().deleteUserRole(1L, 1L);
	}
}
