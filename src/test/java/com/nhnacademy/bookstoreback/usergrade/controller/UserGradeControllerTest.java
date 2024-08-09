package com.nhnacademy.bookstoreback.usergrade.controller;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
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
import com.nhnacademy.bookstoreback.usergrade.domain.dto.request.CreateUserGradeRequest;
import com.nhnacademy.bookstoreback.usergrade.domain.dto.request.UpdateUserGradeRequest;
import com.nhnacademy.bookstoreback.usergrade.domain.dto.response.CreateUserGradeResponse;
import com.nhnacademy.bookstoreback.usergrade.domain.dto.response.GetUserGradeResponse;
import com.nhnacademy.bookstoreback.usergrade.domain.dto.response.UpdateUserGradeResponse;
import com.nhnacademy.bookstoreback.usergrade.service.UserGradeService;

@ExtendWith(MockitoExtension.class)
class UserGradeControllerTest {

	@InjectMocks
	private UserGradeController userGradeController;

	@Mock
	private UserGradeService userGradeService;

	private MockMvc mockMvc;
	private ObjectMapper objectMapper;

	private CreateUserGradeResponse createUserGradeResponse;
	private GetUserGradeResponse getUserGradeResponse;
	private UpdateUserGradeResponse updateUserGradeResponse;

	@BeforeEach
	void setUp() {
		objectMapper = new ObjectMapper();
		mockMvc = MockMvcBuilders.standaloneSetup(userGradeController).build();

		createUserGradeResponse = CreateUserGradeResponse.builder()
			.userGradeName("Silver")
			.userGradeMinAmount(BigDecimal.valueOf(1000))
			.userGradeMaxAmount(BigDecimal.valueOf(5000))
			.userGradePointRate(BigDecimal.valueOf(0.05))
			.build();

		getUserGradeResponse = GetUserGradeResponse.builder()
			.userGradeName("Silver")
			.userGradeMinAmount(BigDecimal.valueOf(1000))
			.userGradeMaxAmount(BigDecimal.valueOf(5000))
			.userGradePointRate(BigDecimal.valueOf(0.05))
			.build();

		updateUserGradeResponse = UpdateUserGradeResponse.builder()
			.userGradeName("Silver")
			.userGradeMinAmount(BigDecimal.valueOf(1000))
			.userGradeMaxAmount(BigDecimal.valueOf(5000))
			.userGradePointRate(BigDecimal.valueOf(0.05))
			.build();
	}

	@Test
	void testCreateUserGrade() throws Exception {
		CreateUserGradeRequest request = CreateUserGradeRequest.builder()
			.userGradeName("Silver")
			.userGradeMinAmount(BigDecimal.valueOf(1000))
			.userGradeMaxAmount(BigDecimal.valueOf(5000))
			.userGradePointRate(BigDecimal.valueOf(0.05))
			.build();

		given(userGradeService.createUserGrade(any(CreateUserGradeRequest.class))).willReturn(createUserGradeResponse);

		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/user-grades")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isCreated())
			.andReturn();

		String expectedJson = objectMapper.writeValueAsString(createUserGradeResponse);
		MockMvcResultMatchers.content().json(expectedJson).match(result);
	}

	@Test
	void testGetUserGrades() throws Exception {
		List<GetUserGradeResponse> userGrades = Collections.singletonList(getUserGradeResponse);
		given(userGradeService.getUserGrades()).willReturn(userGrades);

		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/user-grades")
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andReturn();

		String expectedJson = objectMapper.writeValueAsString(userGrades);
		MockMvcResultMatchers.content().json(expectedJson).match(result);
	}

	@Test
	void testGetUserGrade() throws Exception {
		String userGradeName = "Silver";
		given(userGradeService.getUserGrade(userGradeName)).willReturn(getUserGradeResponse);

		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/user-grades/{userGradeName}", userGradeName)
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andReturn();

		String expectedJson = objectMapper.writeValueAsString(getUserGradeResponse);
		MockMvcResultMatchers.content().json(expectedJson).match(result);
	}

	@Test
	void testUpdateUserGrade() throws Exception {
		String userGradeName = "Silver";
		UpdateUserGradeRequest request = UpdateUserGradeRequest.builder()
			.userGradeMinAmount(BigDecimal.valueOf(2000))
			.userGradeMaxAmount(BigDecimal.valueOf(6000))
			.userGradePointRate(BigDecimal.valueOf(0.07))
			.build();

		given(userGradeService.updateUserGrade(eq(userGradeName), any(UpdateUserGradeRequest.class))).willReturn(
			updateUserGradeResponse);

		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/user-grades/{userGradeName}", userGradeName)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isOk())
			.andReturn();

		String expectedJson = objectMapper.writeValueAsString(updateUserGradeResponse);
		MockMvcResultMatchers.content().json(expectedJson).match(result);
	}

	@Test
	void testDeleteUserGrade() throws Exception {
		String userGradeName = "Silver";

		mockMvc.perform(MockMvcRequestBuilders.delete("/user-grades/{userGradeName}", userGradeName))
			.andExpect(status().isNoContent());

		then(userGradeService).should().deleteUserGrade(userGradeName);
	}
}