package com.nhnacademy.bookstoreback.user.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nhnacademy.bookstoreback.auth.annotation.CurrentUser;
import com.nhnacademy.bookstoreback.auth.jwt.dto.AppCustomUserDetails;
import com.nhnacademy.bookstoreback.user.domain.dto.request.CreateUserRequest;
import com.nhnacademy.bookstoreback.user.domain.dto.request.UpdateUserInfoRequest;
import com.nhnacademy.bookstoreback.user.domain.dto.response.CreateUserResponse;
import com.nhnacademy.bookstoreback.user.domain.dto.response.GetUserInfoResponse;
import com.nhnacademy.bookstoreback.user.domain.dto.response.UpdateUserInfoResponse;
import com.nhnacademy.bookstoreback.user.domain.entity.User;
import com.nhnacademy.bookstoreback.user.exception.UserAlreadyExistsException;
import com.nhnacademy.bookstoreback.user.exception.UserNotFoundException;
import com.nhnacademy.bookstoreback.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	/**
	 * 테스트용 API
	 * 이메일을 기준으로 사용자 정보를 조회합니다.
	 *
	 * @param email 사용자 이메일
	 * @return 사용자 정보 응답
	 */
	public GetUserInfoResponse getUserInfoByEmail(String email) {
		User user = userRepository.findByEmail(email);

		if (user == null) {
			return null;
		}

		return GetUserInfoResponse.fromEntity(user);
	}

	/**
	 * 사용자 정보를 생성합니다.
	 *
	 * @param createUserRequest 사용자 정보: [이름, 이메일, 비밀번호, 생년월일, 연락처]
	 * @return 생성된 사용자 정보 응답
	 */
	public CreateUserResponse createUser(CreateUserRequest createUserRequest) {
		if (userRepository.existsByEmail(createUserRequest.email())) {
			throw new UserAlreadyExistsException(createUserRequest.email());
		}

		String encodedPassword = passwordEncoder.encode(createUserRequest.password());
		User user = User.toEntity(createUserRequest, encodedPassword);
		User savedUser = userRepository.save(user);

		return CreateUserResponse.fromEntity(savedUser);
	}

	/**
	 * 사용자 정보를 수정합니다.
	 *
	 * @param currentUser 현재 요청한 사용자 정보
	 * @param updateUserInfoRequest 사용자 ID, 수정될 것: [이름, 이메일, 비밀번호, 생년월일, 연락처]
	 * @return 수정된 사용자 정보 응답
	 */
	public UpdateUserInfoResponse updateUserInfo(
		@CurrentUser AppCustomUserDetails currentUser,
		UpdateUserInfoRequest updateUserInfoRequest
	) {
		User user = userRepository.findById(updateUserInfoRequest.id())
			.orElseThrow(() -> new UserNotFoundException(updateUserInfoRequest.id()));

		// if (Objects.isNull(currentUser) || !user.getId().equals(currentUser.getUserId())) {
		// 	throw new AccessDeniedException(currentUser.getUserId(), user.getId());
		// }

		UpdateUserInfoRequest updateUserInfoRequestWithEncodedPassword
			= updateUserInfoRequest.encodePassword(passwordEncoder);
		user.update(updateUserInfoRequestWithEncodedPassword);
		User updatedUser = userRepository.save(user);

		return UpdateUserInfoResponse.fromEntity(updatedUser);
	}
}
