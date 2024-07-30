package com.nhnacademy.bookstoreback.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nhnacademy.bookstoreback.user.domain.dto.response.GetPaycoUserTokenInfoResponse;
import com.nhnacademy.bookstoreback.user.domain.dto.response.UserTokenInfo;
import com.nhnacademy.bookstoreback.user.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

/**
 * @author 김태환
 * 내부 사용자 정보를 관리하고 조회하는 API 를 제공하는 컨트롤러입니다.
 */
@Tag(name = "Internal User Management", description = "내부 사용자 정보를 관리하고 조회하는 API")
@RestController
@RequestMapping("/api/internal/users")
@RequiredArgsConstructor
public class InternalUserController {
	private final UserService userService;

	/**
	 * 이메일을 통해 사용자의 토큰 정보를 조회합니다.
	 *
	 * @param userEmail 사용자 이메일을 헤더로 전달합니다.
	 * @return 사용자 정보가 포함된 {@link ResponseEntity} 객체를 반환합니다.
	 */
	@Operation(
		summary = "이메일로 사용자 정보 조회",
		description = "이메일을 통해 사용자의 토큰 정보를 조회합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "사용자 정보가 성공적으로 조회되었습니다."),
		@ApiResponse(responseCode = "404", description = "해당 이메일을 가진 사용자를 찾을 수 없습니다.")
	})
	@GetMapping("/info")
	public ResponseEntity<UserTokenInfo> getUserInfoByEmail(
		@RequestHeader("X-User-Email") String userEmail) {
		UserTokenInfo user = userService.getUserTokenInfoByEmail(userEmail);

		if (user == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}

		return ResponseEntity.status(HttpStatus.OK).body(user);
	}

	/**
	 * Payco ID를 통해 사용자의 토큰 정보를 조회합니다.
	 *
	 * @param paycoIdNo 사용자의 Payco ID를 쿼리 파라미터로 전달합니다.
	 * @return 사용자 정보가 포함된 {@link ResponseEntity} 객체를 반환합니다.
	 */
	@Operation(
		summary = "Payco ID로 사용자 정보 조회",
		description = "Payco ID를 통해 사용자의 토큰 정보를 조회합니다."
	)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "사용자 정보가 성공적으로 조회되었습니다."),
		@ApiResponse(responseCode = "404", description = "해당 Payco ID를 가진 사용자를 찾을 수 없습니다.")
	})
	@GetMapping("/info-by-payco-id")
	public ResponseEntity<GetPaycoUserTokenInfoResponse> getUserInfoByPaycoId(
		@RequestParam("paycoIdNo") String paycoIdNo) {
		GetPaycoUserTokenInfoResponse user = userService.getUserTokenInfoByPaycoId(paycoIdNo);

		if (user == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}

		return ResponseEntity.status(HttpStatus.OK).body(user);
	}
}
