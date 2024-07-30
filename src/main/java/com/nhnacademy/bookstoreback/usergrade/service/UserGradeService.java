package com.nhnacademy.bookstoreback.usergrade.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nhnacademy.bookstoreback.usergrade.domain.dto.request.CreateUserGradeRequest;
import com.nhnacademy.bookstoreback.usergrade.domain.dto.request.UpdateUserGradeRequest;
import com.nhnacademy.bookstoreback.usergrade.domain.dto.response.CreateUserGradeResponse;
import com.nhnacademy.bookstoreback.usergrade.domain.dto.response.GetUserGradeResponse;
import com.nhnacademy.bookstoreback.usergrade.domain.dto.response.UpdateUserGradeResponse;
import com.nhnacademy.bookstoreback.usergrade.domain.entity.UserGrade;
import com.nhnacademy.bookstoreback.usergrade.exception.UserGradeAlreadyExistsException;
import com.nhnacademy.bookstoreback.usergrade.exception.UserGradeNotFoundException;
import com.nhnacademy.bookstoreback.usergrade.repository.UserGradeRepository;

import lombok.RequiredArgsConstructor;

/**
 * @author 김태환
 * 사용자 등급 관련 서비스를 제공하는 인터페이스입니다.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class UserGradeService {
	private final UserGradeRepository userGradeRepository;

	/**
	 * 새로운 사용자 등급을 생성합니다.
	 *
	 * @param createUserGradeRequest 생성할 사용자 등급 요청 DTO
	 * @return 생성된 사용자 등급 응답 DTO
	 */
	public CreateUserGradeResponse createUserGrade(CreateUserGradeRequest createUserGradeRequest) {
		if (userGradeRepository.existsByUserGradeName(createUserGradeRequest.userGradeName())) {
			throw new UserGradeAlreadyExistsException(createUserGradeRequest.userGradeName());
		}

		UserGrade userGrade = UserGrade.toEntity(createUserGradeRequest);
		UserGrade savedUserGrade = userGradeRepository.save(userGrade);

		return CreateUserGradeResponse.fromEntity(savedUserGrade);
	}

	/**
	 * 모든 사용자 등급을 조회합니다.
	 *
	 * @return 사용자 등급 응답 DTO 목록
	 */
	public List<GetUserGradeResponse> getUserGrades() {
		List<UserGrade> userGrades = userGradeRepository.findAll();
		return userGrades.stream()
			.map(GetUserGradeResponse::fromEntity)
			.toList();
	}

	/**
	 * 특정 사용자 등급을 조회합니다.
	 *
	 * @param userGradeName 조회할 사용자 등급 이름
	 * @return 사용자 등급 응답 DTO
	 */
	public GetUserGradeResponse getUserGrade(String userGradeName) {
		UserGrade userGrade = userGradeRepository.findByUserGradeName(userGradeName)
			.orElseThrow(() -> new UserGradeNotFoundException(userGradeName));

		return GetUserGradeResponse.fromEntity(userGrade);
	}

	/**
	 * 특정 사용자 등급을 업데이트합니다.
	 *
	 * @param userGradeName 업데이트할 사용자 등급 이름
	 * @param updateUserGradeRequest 사용자 등급 업데이트 요청 DTO
	 * @return 업데이트된 사용자 등급 응답 DTO
	 */
	public UpdateUserGradeResponse updateUserGrade(String userGradeName,
		UpdateUserGradeRequest updateUserGradeRequest) {
		UserGrade userGrade = userGradeRepository.findByUserGradeName(userGradeName)
			.orElseThrow(() -> new UserGradeNotFoundException(userGradeName));

		userGrade.update(updateUserGradeRequest);
		UserGrade savedUserGrade = userGradeRepository.save(userGrade);

		return UpdateUserGradeResponse.fromEntity(savedUserGrade);
	}

	/**
	 * 특정 사용자 등급을 삭제합니다.
	 *
	 * @param userGradeName 삭제할 사용자 등급 이름
	 */
	public void deleteUserGrade(String userGradeName) {
		UserGrade userGrade = userGradeRepository.findByUserGradeName(userGradeName)
			.orElseThrow(() -> new UserGradeNotFoundException(userGradeName));

		userGradeRepository.delete(userGrade);
	}
}
