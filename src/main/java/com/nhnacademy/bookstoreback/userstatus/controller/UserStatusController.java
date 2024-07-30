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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * @author 김태환
 * 사용자 상태 관리 HTTP 요청을 처리하는 컨트롤러입니다.
 */
@Tag(name = "UserStatus", description = "사용자 상태 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/user-statuses")
public class UserStatusController {
	private final UserStatusService userStatusService;

	/**
	 * 새로운 사용자 상태를 생성합니다.
	 *
	 * @param createUserStatusRequest 생성할 사용자 상태 정보
	 * @return 생성된 사용자 상태 정보
	 */
	@Operation(
		summary = "새로운 사용자 상태 생성",
		description = "새로운 사용자 상태를 생성합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "201", description = "사용자 상태가 성공적으로 생성됨"),
		@ApiResponse(responseCode = "400", description = "잘못된 요청 데이터입니다.")
	})
	@PostMapping
	@AuthorizeRole({"MEMBER_ADMIN", "HEAD_ADMIN"})
	public ResponseEntity<CreateUserStatusResponse> createUserStatus(
		@Valid @RequestBody CreateUserStatusRequest createUserStatusRequest
	) {
		CreateUserStatusResponse createUserStatusResponse = userStatusService.createUserStatus(createUserStatusRequest);
		return ResponseEntity.status(HttpStatus.CREATED).body(createUserStatusResponse);
	}

	/**
	 * 모든 사용자 상태 정보를 조회합니다.
	 *
	 * @return 사용자 상태 정보 리스트
	 */
	@Operation(
		summary = "모든 사용자 상태 조회",
		description = "모든 사용자 상태 정보를 조회합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "사용자 상태 목록 조회 성공")
	})
	@GetMapping
	@AuthorizeRole({"MEMBER_ADMIN", "HEAD_ADMIN"})
	public ResponseEntity<List<GetUserStatusResponse>> getUserStatuses() {
		List<GetUserStatusResponse> getUserStatusResponses = userStatusService.getUserStatuses();
		return ResponseEntity.status(HttpStatus.OK).body(getUserStatusResponses);
	}

	/**
	 * 주어진 사용자 상태 이름에 해당하는 사용자 상태를 삭제합니다.
	 *
	 * @param userStatusName 삭제할 사용자 상태 이름
	 * @return 응답 상태 코드 (204 NO CONTENT)
	 */
	@Operation(
		summary = "사용자 상태 삭제",
		description = "주어진 사용자 상태 이름에 대한 사용자 상태를 삭제합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "204", description = "사용자 상태가 성공적으로 삭제됨"),
		@ApiResponse(responseCode = "404", description = "요청한 사용자 상태를 찾을 수 없음")
	})
	@DeleteMapping("/{userStatusName}")
	@AuthorizeRole({"MEMBER_ADMIN", "HEAD_ADMIN"})
	public ResponseEntity<Void> deleteUserStatus(@PathVariable String userStatusName) {
		userStatusService.deleteUserStatus(userStatusName);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
}
