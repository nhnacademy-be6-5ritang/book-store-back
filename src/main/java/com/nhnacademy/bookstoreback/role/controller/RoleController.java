package com.nhnacademy.bookstoreback.role.controller;

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

import com.nhnacademy.bookstoreback.role.domain.dto.request.CreateRoleRequest;
import com.nhnacademy.bookstoreback.role.domain.dto.response.CreateRoleResponse;
import com.nhnacademy.bookstoreback.role.domain.dto.response.GetRoleResponse;
import com.nhnacademy.bookstoreback.role.service.RoleService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/roles")
public class RoleController {
	private final RoleService roleService;

	@PostMapping
	public ResponseEntity<CreateRoleResponse> createRole(@RequestBody CreateRoleRequest createRoleRequest) {
		CreateRoleResponse createRoleResponse = roleService.createRole(createRoleRequest);
		return ResponseEntity.status(HttpStatus.CREATED).body(createRoleResponse);
	}

	@GetMapping
	public ResponseEntity<List<GetRoleResponse>> getRoles() {
		List<GetRoleResponse> getRoleResponses = roleService.getRoles();
		return ResponseEntity.ok(getRoleResponses);
	}

	@DeleteMapping("/{roleName}")
	public ResponseEntity<Void> deleteRole(@PathVariable String roleName) {
		roleService.deleteRole(roleName);
		return ResponseEntity.noContent().build();
	}
}
