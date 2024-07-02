package com.nhnacademy.bookstoreback.usergrade.service;

import java.util.List;
import java.util.stream.Collectors;

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

@Service
@RequiredArgsConstructor
@Transactional
public class UserGradeService {
	private final UserGradeRepository userGradeRepository;

	public CreateUserGradeResponse createUserGrade(CreateUserGradeRequest createUserGradeRequest) {
		if (userGradeRepository.existsByUserGradeName(createUserGradeRequest.userGradeName())) {
			throw new UserGradeAlreadyExistsException(createUserGradeRequest.userGradeName());
		}

		UserGrade userGrade = UserGrade.toEntity(createUserGradeRequest);
		UserGrade savedUserGrade = userGradeRepository.save(userGrade);

		return CreateUserGradeResponse.fromEntity(savedUserGrade);
	}

	public List<GetUserGradeResponse> getUserGrades() {
		List<UserGrade> userGrades = userGradeRepository.findAll();
		return userGrades.stream()
			.map(GetUserGradeResponse::fromEntity)
			.collect(Collectors.toList());
	}

	public GetUserGradeResponse getUserGrade(String userGradeName) {
		UserGrade userGrade = userGradeRepository.findByUserGradeName(userGradeName)
			.orElseThrow(() -> new UserGradeNotFoundException(userGradeName));

		return GetUserGradeResponse.fromEntity(userGrade);
	}

	public UpdateUserGradeResponse updateUserGrade(String userGradeName,
		UpdateUserGradeRequest updateUserGradeRequest) {
		UserGrade userGrade = userGradeRepository.findByUserGradeName(userGradeName)
			.orElseThrow(() -> new UserGradeNotFoundException(userGradeName));

		userGrade.update(updateUserGradeRequest);
		UserGrade savedUserGrade = userGradeRepository.save(userGrade);

		return UpdateUserGradeResponse.fromEntity(savedUserGrade);
	}

	public void deleteUserGrade(String userGradeName) {
		UserGrade userGrade = userGradeRepository.findByUserGradeName(userGradeName)
			.orElseThrow(() -> new UserGradeNotFoundException(userGradeName));

		userGradeRepository.delete(userGrade);
	}
}
