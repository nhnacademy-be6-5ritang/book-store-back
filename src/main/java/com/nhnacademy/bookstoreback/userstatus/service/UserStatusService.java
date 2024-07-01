package com.nhnacademy.bookstoreback.userstatus.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nhnacademy.bookstoreback.userstatus.domain.dto.request.CreateUserStatusRequest;
import com.nhnacademy.bookstoreback.userstatus.domain.dto.response.CreateUserStatusResponse;
import com.nhnacademy.bookstoreback.userstatus.domain.dto.response.GetUserStatusResponse;
import com.nhnacademy.bookstoreback.userstatus.domain.entity.UserStatus;
import com.nhnacademy.bookstoreback.userstatus.exception.UserStatusAlreadyExistsException;
import com.nhnacademy.bookstoreback.userstatus.repository.UserStatusRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class UserStatusService {
	private final UserStatusRepository userStatusRepository;

	public CreateUserStatusResponse createUserStatus(CreateUserStatusRequest createUserStatusRequest) {
		if (userStatusRepository.existsByUserStatusName(createUserStatusRequest.userStatusName())) {
			throw new UserStatusAlreadyExistsException(createUserStatusRequest.userStatusName());
		}

		UserStatus userStatus = UserStatus.toEntity(createUserStatusRequest);
		UserStatus savedUserStatus = userStatusRepository.save(userStatus);

		return CreateUserStatusResponse.fromEntity(savedUserStatus);
	}

	public List<GetUserStatusResponse> getUserStatuses() {
		List<UserStatus> userStatuses = userStatusRepository.findAll();

		return userStatuses.stream()
			.map(GetUserStatusResponse::fromEntity)
			.toList();
	}

	public void deleteUserStatus(String userStatusName) {
		userStatusRepository.deleteByUserStatusName(userStatusName);
	}
}
