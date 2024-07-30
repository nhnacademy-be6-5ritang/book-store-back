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

/**
 * @author 김태환
 * 사용자 상태 관련 서비스를 제공하는 인터페이스입니다.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class UserStatusService {
	private final UserStatusRepository userStatusRepository;

	/**
	 * 새로운 사용자 상태를 생성합니다.
	 *
	 * @param createUserStatusRequest 생성할 사용자 상태 정보 요청 DTO
	 * @return 생성된 사용자 상태 응답 DTO
	 */
	public CreateUserStatusResponse createUserStatus(CreateUserStatusRequest createUserStatusRequest) {
		if (userStatusRepository.existsByUserStatusName(createUserStatusRequest.userStatusName())) {
			throw new UserStatusAlreadyExistsException(createUserStatusRequest.userStatusName());
		}

		UserStatus userStatus = UserStatus.toEntity(createUserStatusRequest);
		UserStatus savedUserStatus = userStatusRepository.save(userStatus);

		return CreateUserStatusResponse.fromEntity(savedUserStatus);
	}

	/**
	 * 모든 사용자 상태를 조회합니다.
	 *
	 * @return 사용자 상태 응답 DTO 목록
	 */
	public List<GetUserStatusResponse> getUserStatuses() {
		List<UserStatus> userStatuses = userStatusRepository.findAll();

		return userStatuses.stream()
			.map(GetUserStatusResponse::fromEntity)
			.toList();
	}

	/**
	 * 특정 사용자 상태를 삭제합니다.
	 *
	 * @param userStatusName 삭제할 사용자 상태 이름
	 */
	public void deleteUserStatus(String userStatusName) {
		userStatusRepository.deleteByUserStatusName(userStatusName);
	}
}
