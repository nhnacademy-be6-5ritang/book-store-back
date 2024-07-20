package com.nhnacademy.bookstoreback.userstatus.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nhnacademy.bookstoreback.auth.annotation.AuthorizeRole;
import com.nhnacademy.bookstoreback.userstatus.domain.dto.request.CreateUserStatusRequest;
import com.nhnacademy.bookstoreback.userstatus.domain.dto.response.CreateUserStatusResponse;
import com.nhnacademy.bookstoreback.userstatus.domain.dto.response.GetUserStatusResponse;
import com.nhnacademy.bookstoreback.userstatus.service.UserStatusService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user-statuses")
public class UserStatusController {
	private final UserStatusService userStatusService;

	@PostMapping
	@AuthorizeRole({"MEMBER_ADMIN", "HEAD_ADMIN"})
	public ResponseEntity<CreateUserStatusResponse> createUserStatus(
		@Valid @RequestBody CreateUserStatusRequest createUserStatusRequest
	) {
		CreateUserStatusResponse createUserStatusResponse = userStatusService.createUserStatus(createUserStatusRequest);
		return ResponseEntity.status(HttpStatus.CREATED).body(createUserStatusResponse);
	}

	@GetMapping
	@AuthorizeRole({"MEMBER_ADMIN", "HEAD_ADMIN"})
	public ResponseEntity<List<GetUserStatusResponse>> getUserStatuses() {
		List<GetUserStatusResponse> getUserStatusResponses = userStatusService.getUserStatuses();
		return ResponseEntity.status(HttpStatus.OK).body(getUserStatusResponses);
	}

	@DeleteMapping("/{userStatusName}")
	@AuthorizeRole({"MEMBER_ADMIN", "HEAD_ADMIN"})
	public ResponseEntity<Void> deleteUserStatus(@PathVariable String userStatusName) {
		userStatusService.deleteUserStatus(userStatusName);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
}
