package com.nhnacademy.bookstoreback.user.service;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nhnacademy.bookstoreback.global.exception.UserAlreadyExistsException;
import com.nhnacademy.bookstoreback.global.exception.payload.ErrorStatus;
import com.nhnacademy.bookstoreback.user.domain.dto.request.CreateUserRequest;
import com.nhnacademy.bookstoreback.user.domain.dto.response.CreateUserResponse;
import com.nhnacademy.bookstoreback.user.domain.entity.User;
import com.nhnacademy.bookstoreback.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public CreateUserResponse createUser(CreateUserRequest createUserRequest) {
		if (userRepository.existsByEmail(createUserRequest.email())) {
			String errorMessage = String.format("해당 이메일 '%s'는 이미 사용 중인 이메일입니다.", createUserRequest.email());
			ErrorStatus errorStatus = ErrorStatus.from(errorMessage, HttpStatus.CONFLICT, LocalDateTime.now());
			throw new UserAlreadyExistsException(errorStatus);
		}

		String encodedPassword = passwordEncoder.encode(createUserRequest.password());
		User user = User.toEntity(createUserRequest, encodedPassword);
		User savedUser = userRepository.save(user);

		return CreateUserResponse.fromEntity(savedUser);
	}

	// public UpdateUserInfoResponse updateUserInfo(UpdateUserInfoRequest updateUserInfoRequest) {
	// 	User user = userRepository.findById(updateUserInfoRequest.id())
	// 		.orElseThrow(() -> new UserNotFoundException(updateUserInfoRequest.id()));
	//
	// 	user.update(updateUserInfoRequest);
	//
	// 	return UpdateUserInfoResponse.fromEntity(user);
	// }
}
