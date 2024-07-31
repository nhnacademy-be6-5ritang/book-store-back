package com.nhnacademy.bookstoreback.userstatus.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.nhnacademy.bookstoreback.userstatus.domain.dto.request.CreateUserStatusRequest;
import com.nhnacademy.bookstoreback.userstatus.domain.dto.response.CreateUserStatusResponse;
import com.nhnacademy.bookstoreback.userstatus.domain.dto.response.GetUserStatusResponse;
import com.nhnacademy.bookstoreback.userstatus.domain.entity.UserStatus;
import com.nhnacademy.bookstoreback.userstatus.exception.UserStatusAlreadyExistsException;
import com.nhnacademy.bookstoreback.userstatus.repository.UserStatusRepository;

@ExtendWith(MockitoExtension.class)
class UserStatusServiceTest {

	@InjectMocks
	private UserStatusService userStatusService;

	@Mock
	private UserStatusRepository userStatusRepository;

	private CreateUserStatusRequest createUserStatusRequest;
	private UserStatus userStatus;

	@BeforeEach
	void setUp() {
		createUserStatusRequest = CreateUserStatusRequest.builder()
			.userStatusName("Active")
			.build();

		userStatus = UserStatus.builder()
			.userStatusName("Active")
			.build();
	}

	@Test
	void testCreateUserStatus_Success() {
		given(userStatusRepository.existsByUserStatusName(createUserStatusRequest.userStatusName())).willReturn(false);
		given(userStatusRepository.save(any(UserStatus.class))).willReturn(userStatus);

		CreateUserStatusResponse response = userStatusService.createUserStatus(createUserStatusRequest);

		assertNotNull(response);
		assertEquals(createUserStatusRequest.userStatusName(), response.userStatusName());
		verify(userStatusRepository).existsByUserStatusName(createUserStatusRequest.userStatusName());
		verify(userStatusRepository).save(any(UserStatus.class));
	}

	@Test
	void testCreateUserStatus_UserStatusAlreadyExists() {
		given(userStatusRepository.existsByUserStatusName(createUserStatusRequest.userStatusName())).willReturn(true);

		assertThrows(UserStatusAlreadyExistsException.class, () -> {
			userStatusService.createUserStatus(createUserStatusRequest);
		});

		verify(userStatusRepository).existsByUserStatusName(createUserStatusRequest.userStatusName());
		verify(userStatusRepository, never()).save(any(UserStatus.class));
	}

	@Test
	void testGetUserStatuses() {
		List<UserStatus> userStatuses = List.of(userStatus);
		given(userStatusRepository.findAll()).willReturn(userStatuses);

		List<GetUserStatusResponse> responses = userStatusService.getUserStatuses();

		assertNotNull(responses);
		assertEquals(1, responses.size());
		assertEquals(userStatus.getUserStatusName(), responses.get(0).userStatusName());
		verify(userStatusRepository).findAll();
	}

	@Test
	void testDeleteUserStatus() {
		String userStatusName = "Active";

		willDoNothing().given(userStatusRepository).deleteByUserStatusName(userStatusName);

		userStatusService.deleteUserStatus(userStatusName);

		verify(userStatusRepository).deleteByUserStatusName(userStatusName);
	}
}