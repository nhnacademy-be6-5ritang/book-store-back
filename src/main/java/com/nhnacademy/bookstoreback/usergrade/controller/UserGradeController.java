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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * @author 김태환
 * 사용자 등급 관리 HTTP 요청을 처리하는 컨트롤러입니다.
 */
@Tag(name = "UserGrade", description = "사용자 등급 API")
@RestController
@RequestMapping("/api/user-grades")
@RequiredArgsConstructor
public class UserGradeController {
	private final UserGradeService userGradeService;

	/**
	 * 새로운 사용자 등급을 생성합니다.
	 *
	 * @param createUserGradeRequest 생성할 사용자 등급 정보 DTO
	 * @return 생성된 사용자 등급 정보
	 */
	@Operation(
		summary = "새로운 사용자 등급 생성",
		description = "새로운 사용자 등급을 생성합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "201", description = "사용자 등급이 성공적으로 생성됨"),
		@ApiResponse(responseCode = "400", description = "잘못된 요청 데이터입니다.")
	})
	@PostMapping
	@AuthorizeRole({"MEMBER_ADMIN", "HEAD_ADMIN"})
	public ResponseEntity<CreateUserGradeResponse> createUserGrade(
		@Valid @RequestBody CreateUserGradeRequest createUserGradeRequest) {
		CreateUserGradeResponse createUserGradeResponse = userGradeService.createUserGrade(createUserGradeRequest);
		return ResponseEntity.status(HttpStatus.CREATED).body(createUserGradeResponse);
	}
	
	/**
	 * 모든 사용자 등급 정보를 조회합니다.
	 *
	 * @return 모든 사용자 등급 정보 리스트
	 */
	@Operation(
		summary = "모든 사용자 등급 조회",
		description = "모든 사용자 등급 정보를 조회합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "사용자 등급 목록 조회 성공")
	})
	@GetMapping
	public ResponseEntity<List<GetUserGradeResponse>> getUserGrades() {
		List<GetUserGradeResponse> getUserGradeResponses = userGradeService.getUserGrades();
		return ResponseEntity.status(HttpStatus.OK).body(getUserGradeResponses);
	}

	/**
	 * 주어진 사용자 등급 이름에 해당하는 사용자 등급 정보를 조회합니다.
	 *
	 * @param userGradeName 조회할 사용자 등급 이름
	 * @return 해당 사용자 등급 정보
	 */
	@Operation(
		summary = "특정 사용자 등급 조회",
		description = "주어진 사용자 등급 이름에 대한 사용자 등급 정보를 조회합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "사용자 등급 조회 성공"),
		@ApiResponse(responseCode = "404", description = "요청한 사용자 등급을 찾을 수 없음")
	})
	@GetMapping("/{userGradeName}")
	@AuthorizeRole({"MEMBER_ADMIN", "HEAD_ADMIN"})
	public ResponseEntity<GetUserGradeResponse> getUserGrade(@PathVariable String userGradeName) {
		GetUserGradeResponse getUserGradeResponse = userGradeService.getUserGrade(userGradeName);
		return ResponseEntity.status(HttpStatus.OK).body(getUserGradeResponse);
	}

	/**
	 * 주어진 사용자 등급 이름에 해당하는 사용자 등급 정보를 업데이트합니다.
	 *
	 * @param userGradeName 업데이트할 사용자 등급 이름
	 * @param updateUserGradeRequest 업데이트할 사용자 등급 정보 DTO
	 * @return 업데이트된 사용자 등급 정보
	 */
	@Operation(
		summary = "사용자 등급 업데이트",
		description = "주어진 사용자 등급 이름에 대한 사용자 등급 정보를 업데이트합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "사용자 등급 정보가 성공적으로 업데이트됨"),
		@ApiResponse(responseCode = "400", description = "잘못된 요청 데이터입니다."),
		@ApiResponse(responseCode = "404", description = "요청한 사용자 등급을 찾을 수 없음")
	})
	@PutMapping("/{userGradeName}")
	@AuthorizeRole({"MEMBER_ADMIN", "HEAD_ADMIN"})
	public ResponseEntity<UpdateUserGradeResponse> updateUserGrade(
		@PathVariable String userGradeName, @Valid @RequestBody UpdateUserGradeRequest updateUserGradeRequest
	) {
		UpdateUserGradeResponse updateUserGradeResponse
			= userGradeService.updateUserGrade(userGradeName, updateUserGradeRequest);

		return ResponseEntity.status(HttpStatus.OK).body(updateUserGradeResponse);
	}

	/**
	 * 주어진 사용자 등급 이름에 해당하는 사용자 등급을 삭제합니다.
	 *
	 * @param userGradeName 삭제할 사용자 등급 이름
	 * @return 응답 상태 코드 (204 NO CONTENT)
	 */
	@Operation(
		summary = "사용자 등급 삭제",
		description = "주어진 사용자 등급 이름에 대한 사용자 등급을 삭제합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "204", description = "사용자 등급이 성공적으로 삭제됨")
	})
	@DeleteMapping("/{userGradeName}")
	@AuthorizeRole({"MEMBER_ADMIN", "HEAD_ADMIN"})
	public ResponseEntity<Void> deleteUserGrade(@PathVariable String userGradeName) {
		userGradeService.deleteUserGrade(userGradeName);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
}
