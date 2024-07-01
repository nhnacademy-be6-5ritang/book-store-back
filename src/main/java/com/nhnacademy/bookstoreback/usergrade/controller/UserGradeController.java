package com.nhnacademy.bookstoreback.usergrade.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nhnacademy.bookstoreback.usergrade.domain.dto.request.CreateUserGradeRequest;
import com.nhnacademy.bookstoreback.usergrade.domain.dto.request.UpdateUserGradeRequest;
import com.nhnacademy.bookstoreback.usergrade.domain.dto.response.CreateUserGradeResponse;
import com.nhnacademy.bookstoreback.usergrade.domain.dto.response.GetUserGradeResponse;
import com.nhnacademy.bookstoreback.usergrade.domain.dto.response.UpdateUserGradeResponse;
import com.nhnacademy.bookstoreback.usergrade.service.UserGradeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/user-grades")
@RequiredArgsConstructor
public class UserGradeController {
	private final UserGradeService userGradeService;

	@PostMapping
	public ResponseEntity<CreateUserGradeResponse> createUserGrade(
		@RequestBody CreateUserGradeRequest createUserGradeRequest) {
		CreateUserGradeResponse createUserGradeResponse = userGradeService.createUserGrade(createUserGradeRequest);
		return ResponseEntity.ok(createUserGradeResponse);
	}

	@GetMapping
	public ResponseEntity<List<GetUserGradeResponse>> getUserGrades() {
		List<GetUserGradeResponse> getUserGradeResponses = userGradeService.getUserGrades();
		return ResponseEntity.ok(getUserGradeResponses);
	}

	@GetMapping("/{userGradeName}")
	public ResponseEntity<GetUserGradeResponse> getUserGrade(@PathVariable String userGradeName) {
		GetUserGradeResponse getUserGradeResponse = userGradeService.getUserGrade(userGradeName);
		return ResponseEntity.ok(getUserGradeResponse);
	}

	@PutMapping("/{userGradeName}")
	public ResponseEntity<UpdateUserGradeResponse> updateUserGrade(
		@PathVariable String userGradeName, @RequestBody UpdateUserGradeRequest updateUserGradeRequest
	) {
		UpdateUserGradeResponse updateUserGradeResponse
			= userGradeService.updateUserGrade(userGradeName, updateUserGradeRequest);

		return ResponseEntity.ok(updateUserGradeResponse);
	}

	@DeleteMapping("/{userGradeName}")
	public ResponseEntity<Void> deleteUserGrade(@PathVariable String userGradeName) {
		userGradeService.deleteUserGrade(userGradeName);
		return ResponseEntity.noContent().build();
	}
}
