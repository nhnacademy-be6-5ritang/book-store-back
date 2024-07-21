package com.nhnacademy.bookstoreback.usergrade.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nhnacademy.bookstoreback.auth.annotation.AuthorizeRole;
import com.nhnacademy.bookstoreback.usergrade.domain.dto.request.CreateUserGradeRequest;
import com.nhnacademy.bookstoreback.usergrade.domain.dto.request.UpdateUserGradeRequest;
import com.nhnacademy.bookstoreback.usergrade.domain.dto.response.CreateUserGradeResponse;
import com.nhnacademy.bookstoreback.usergrade.domain.dto.response.GetUserGradeResponse;
import com.nhnacademy.bookstoreback.usergrade.domain.dto.response.UpdateUserGradeResponse;
import com.nhnacademy.bookstoreback.usergrade.service.UserGradeService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/user-grades")
@RequiredArgsConstructor
public class UserGradeController {
	private final UserGradeService userGradeService;

	@PostMapping
	@AuthorizeRole({"MEMBER_ADMIN", "HEAD_ADMIN"})
	public ResponseEntity<CreateUserGradeResponse> createUserGrade(
		@Valid @RequestBody CreateUserGradeRequest createUserGradeRequest) {
		CreateUserGradeResponse createUserGradeResponse = userGradeService.createUserGrade(createUserGradeRequest);
		return ResponseEntity.status(HttpStatus.CREATED).body(createUserGradeResponse);
	}

	@GetMapping
	@AuthorizeRole({"MEMBER_ADMIN", "HEAD_ADMIN"})
	public ResponseEntity<List<GetUserGradeResponse>> getUserGrades() {
		List<GetUserGradeResponse> getUserGradeResponses = userGradeService.getUserGrades();
		return ResponseEntity.status(HttpStatus.OK).body(getUserGradeResponses);
	}

	@GetMapping("/{userGradeName}")
	@AuthorizeRole({"MEMBER_ADMIN", "HEAD_ADMIN"})
	public ResponseEntity<GetUserGradeResponse> getUserGrade(@PathVariable String userGradeName) {
		GetUserGradeResponse getUserGradeResponse = userGradeService.getUserGrade(userGradeName);
		return ResponseEntity.status(HttpStatus.OK).body(getUserGradeResponse);
	}

	@PutMapping("/{userGradeName}")
	@AuthorizeRole({"MEMBER_ADMIN", "HEAD_ADMIN"})
	public ResponseEntity<UpdateUserGradeResponse> updateUserGrade(
		@PathVariable String userGradeName, @Valid @RequestBody UpdateUserGradeRequest updateUserGradeRequest
	) {
		UpdateUserGradeResponse updateUserGradeResponse
			= userGradeService.updateUserGrade(userGradeName, updateUserGradeRequest);

		return ResponseEntity.status(HttpStatus.OK).body(updateUserGradeResponse);
	}

	@DeleteMapping("/{userGradeName}")
	@AuthorizeRole({"MEMBER_ADMIN", "HEAD_ADMIN"})
	public ResponseEntity<Void> deleteUserGrade(@PathVariable String userGradeName) {
		userGradeService.deleteUserGrade(userGradeName);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
}
