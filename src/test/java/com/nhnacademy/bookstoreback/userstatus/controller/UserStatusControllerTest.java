package com.nhnacademy.bookstoreback.userstatus.controller;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Collections;
import java.util.List;

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
import com.nhnacademy.bookstoreback.userstatus.domain.dto.request.CreateUserStatusRequest;
import com.nhnacademy.bookstoreback.userstatus.domain.dto.response.CreateUserStatusResponse;
import com.nhnacademy.bookstoreback.userstatus.domain.dto.response.GetUserStatusResponse;
import com.nhnacademy.bookstoreback.userstatus.service.UserStatusService;

@ExtendWith(MockitoExtension.class)
class UserStatusControllerTest {

	@InjectMocks
	private UserStatusController userStatusController;

	@Mock
	private UserStatusService userStatusService;

	private MockMvc mockMvc;
	private ObjectMapper objectMapper;

	private CreateUserStatusRequest createUserStatusRequest;
	private CreateUserStatusResponse createUserStatusResponse;
	private GetUserStatusResponse getUserStatusResponse;

	@BeforeEach
	void setUp() {
		objectMapper = new ObjectMapper();
		mockMvc = MockMvcBuilders.standaloneSetup(userStatusController).build();

		createUserStatusRequest = CreateUserStatusRequest.builder()
			.userStatusName("Active")
			.build();

		createUserStatusResponse = CreateUserStatusResponse.builder()
			.userStatusName("Active")
			.build();

		getUserStatusResponse = GetUserStatusResponse.builder()
			.userStatusName("Active")
			.build();
	}

	@Test
	void testCreateUserStatus() throws Exception {
		given(userStatusService.createUserStatus(createUserStatusRequest)).willReturn(createUserStatusResponse);

		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/user-statuses")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(createUserStatusRequest)))
			.andExpect(status().isCreated())
			.andReturn();

		String expectedJson = objectMapper.writeValueAsString(createUserStatusResponse);
		MockMvcResultMatchers.content().json(expectedJson).match(result);
	}

	@Test
	void testGetUserStatuses() throws Exception {
		List<GetUserStatusResponse> userStatusResponses = Collections.singletonList(getUserStatusResponse);
		given(userStatusService.getUserStatuses()).willReturn(userStatusResponses);

		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/user-statuses")
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andReturn();

		String expectedJson = objectMapper.writeValueAsString(userStatusResponses);
		MockMvcResultMatchers.content().json(expectedJson).match(result);
	}

	@Test
	void testDeleteUserStatus() throws Exception {
		String userStatusName = "Active";

		mockMvc.perform(MockMvcRequestBuilders.delete("/user-statuses/{userStatusName}", userStatusName))
			.andExpect(status().isNoContent());

		then(userStatusService).should().deleteUserStatus(userStatusName);
	}
}