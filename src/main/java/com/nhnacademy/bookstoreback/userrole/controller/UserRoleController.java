package com.nhnacademy.bookstoreback.userrole.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nhnacademy.bookstoreback.auth.annotation.AuthorizeRole;
import com.nhnacademy.bookstoreback.userrole.domain.dto.response.AddUserRoleResponse;
import com.nhnacademy.bookstoreback.userrole.domain.dto.response.GetUserRoleResponse;
import com.nhnacademy.bookstoreback.userrole.service.UserRoleService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;

/**
 * @author 김태환
 * 사용자 역할 관리 HTTP 요청을 처리하는 컨트롤러입니다.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user-roles")
public class UserRoleController {
	private final UserRoleService userRoleService;

	/**
	 * 특정 사용자에게 역할을 추가합니다.
	 *
	 * @param userId 사용자 ID
	 * @param roleId 역할 ID
	 * @return 추가된 사용자 역할 정보
	 */
	@Operation(
		summary = "사용자 역할 추가",
		description = "특정 사용자에게 역할을 추가합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "201", description = "사용자 역할이 성공적으로 추가됨"),
		@ApiResponse(responseCode = "400", description = "잘못된 요청 데이터입니다.")
	})
	@PostMapping("/user/{userId}/role/{roleId}")
	@AuthorizeRole({"MEMBER_ADMIN", "HEAD_ADMIN"})
	public ResponseEntity<AddUserRoleResponse> addUserRole(@PathVariable Long userId, @PathVariable Long roleId) {
		AddUserRoleResponse addUserRoleResponse = userRoleService.addUserRole(userId, roleId);
		return ResponseEntity.status(HttpStatus.CREATED).body(addUserRoleResponse);
	}

	/**
	 * 특정 사용자의 모든 역할 정보를 조회합니다.
	 *
	 * @param userId 사용자 ID
	 * @return 사용자 역할 정보
	 */
	@Operation(
		summary = "사용자 역할 조회",
		description = "특정 사용자의 모든 역할 정보를 조회합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "사용자 역할 조회 성공"),
		@ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음")
	})
	@GetMapping("/user/{userId}")
	@AuthorizeRole({"MEMBER_ADMIN", "HEAD_ADMIN"})
	public ResponseEntity<GetUserRoleResponse> getUserRoles(@PathVariable Long userId) {
		GetUserRoleResponse getUserRoleResponses = userRoleService.getUserRoles(userId);
		return ResponseEntity.ok(getUserRoleResponses);
	}

	/**
	 * 특정 사용자에서 역할을 제거합니다.
	 *
	 * @param userId 사용자 ID
	 * @param roleId 역할 ID
	 * @return 응답 상태 코드 (204 NO CONTENT)
	 */
	@Operation(
		summary = "사용자 역할 제거",
		description = "특정 사용자에서 역할을 제거합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "204", description = "사용자 역할이 성공적으로 제거됨"),
		@ApiResponse(responseCode = "404", description = "사용자 또는 역할을 찾을 수 없음")
	})
	@DeleteMapping("/user/{userId}/role/{roleId}")
	@AuthorizeRole({"MEMBER_ADMIN", "HEAD_ADMIN"})
	public ResponseEntity<Void> removeUserRole(@PathVariable Long userId, @PathVariable Long roleId) {
		userRoleService.deleteUserRole(userId, roleId);
		return ResponseEntity.noContent().build();
	}
}
