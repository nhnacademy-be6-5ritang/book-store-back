package com.nhnacademy.bookstoreback.userrole.domain.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nhnacademy.bookstoreback.userrole.domain.dto.response.AddUserRoleResponse;
import com.nhnacademy.bookstoreback.userrole.domain.dto.response.GetUserRoleResponse;
import com.nhnacademy.bookstoreback.userrole.domain.service.UserRoleService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user-roles")
public class UserRoleController {
	private final UserRoleService userRoleService;

	@PostMapping("/user/{userId}/role/{roleId}")
	public ResponseEntity<AddUserRoleResponse> addUserRole(@PathVariable Long userId, @PathVariable Long roleId) {
		AddUserRoleResponse addUserRoleResponse = userRoleService.addUserRole(userId, roleId);
		return ResponseEntity.status(HttpStatus.CREATED).body(addUserRoleResponse);
	}

	@GetMapping("/user/{userId}")
	public ResponseEntity<GetUserRoleResponse> getUserRoles(@PathVariable Long userId) {
		GetUserRoleResponse getUserRoleResponses = userRoleService.getUserRoles(userId);
		return ResponseEntity.ok(getUserRoleResponses);
	}

	@DeleteMapping("/user/{userId}/role/{roleId}")
	public ResponseEntity<Void> removeUserRole(@PathVariable Long userId, @PathVariable Long roleId) {
		userRoleService.deleteUserRole(userId, roleId);
		return ResponseEntity.noContent().build();
	}
}
