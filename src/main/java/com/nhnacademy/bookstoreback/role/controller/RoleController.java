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

import com.nhnacademy.bookstoreback.auth.annotation.AuthorizeRole;
import com.nhnacademy.bookstoreback.role.domain.dto.request.CreateRoleRequest;
import com.nhnacademy.bookstoreback.role.domain.dto.response.CreateRoleResponse;
import com.nhnacademy.bookstoreback.role.domain.dto.response.GetRoleResponse;
import com.nhnacademy.bookstoreback.role.service.RoleService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * @author 김태환
 * 엔티티와 관련된 API를 제공하는 컨트롤러 클래스입니다.
 */
@Tag(name = "Role", description = "역할 관리 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/roles")
public class RoleController {
	private final RoleService roleService;

	/**
	 * 새로운 역할을 생성합니다.
	 *
	 * @param createRoleRequest 생성할 역할 정보
	 * @return 생성된 역할의 정보
	 */
	@Operation(
		summary = "새로운 역할 생성",
		description = "새로운 역할을 생성합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "201", description = "역할이 성공적으로 생성되었습니다."),
		@ApiResponse(responseCode = "400", description = "잘못된 요청 데이터입니다."),
		@ApiResponse(responseCode = "409", description = "이미 존재하는 역할입니다.")
	})
	@PostMapping
	@AuthorizeRole({"MEMBER_ADMIN", "HEAD_ADMIN"})
	public ResponseEntity<CreateRoleResponse> createRole(@Valid @RequestBody CreateRoleRequest createRoleRequest) {
		CreateRoleResponse createRoleResponse = roleService.createRole(createRoleRequest);
		return ResponseEntity.status(HttpStatus.CREATED).body(createRoleResponse);
	}

	/**
	 * 모든 역할을 조회합니다.
	 *
	 * @return 모든 역할의 목록
	 */
	@Operation(
		summary = "모든 역할 조회",
		description = "저장된 모든 역할을 조회합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "모든 역할이 성공적으로 반환되었습니다.")
	})
	@GetMapping
	@AuthorizeRole({"MEMBER_ADMIN", "HEAD_ADMIN"})
	public ResponseEntity<List<GetRoleResponse>> getRoles() {
		List<GetRoleResponse> getRoleResponses = roleService.getRoles();
		return ResponseEntity.ok(getRoleResponses);
	}

	/**
	 * 주어진 역할 이름으로 역할을 삭제합니다.
	 *
	 * @param roleName 삭제할 역할의 이름
	 */
	@Operation(
		summary = "역할 삭제",
		description = "주어진 역할 이름으로 역할을 삭제합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "204", description = "역할이 성공적으로 삭제되었습니다."),
		@ApiResponse(responseCode = "404", description = "요청한 역할을 찾을 수 없습니다.")
	})
	@DeleteMapping("/{roleName}")
	@AuthorizeRole({"MEMBER_ADMIN", "HEAD_ADMIN"})
	public ResponseEntity<Void> deleteRole(@PathVariable String roleName) {
		roleService.deleteRole(roleName);
		return ResponseEntity.noContent().build();
	}
}
