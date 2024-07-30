package com.nhnacademy.bookstoreback.address.controller;

import java.util.List;
import java.util.Optional;

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

import com.nhnacademy.bookstoreback.address.domain.dto.request.RegisterAddressRequest;
import com.nhnacademy.bookstoreback.address.domain.dto.request.UpdateAddressRequest;
import com.nhnacademy.bookstoreback.address.domain.dto.response.GetAddressResponse;
import com.nhnacademy.bookstoreback.address.domain.dto.response.RegisterAddressResponse;
import com.nhnacademy.bookstoreback.address.domain.dto.response.UpdateAddressResponse;
import com.nhnacademy.bookstoreback.address.service.AddressService;
import com.nhnacademy.bookstoreback.auth.annotation.AuthorizeRole;
import com.nhnacademy.bookstoreback.auth.annotation.CurrentUser;
import com.nhnacademy.bookstoreback.auth.jwt.dto.CurrentUserDetails;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "Address", description = "주소 관리 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/addresses")
public class AddressController {
	private final AddressService addressService;

	/**
	 * 새로운 주소를 등록합니다.
	 *
	 * @param currentUser 현재 로그인된 사용자 정보
	 * @param registerAddressRequest 등록할 주소 정보
	 * @return 등록된 주소에 대한 응답
	 */
	@Operation(
		summary = "주소 등록",
		description = "새로운 주소를 등록합니다.",
		responses = {
			@ApiResponse(responseCode = "201", description = "주소가 성공적으로 등록되었습니다."),
			@ApiResponse(responseCode = "400", description = "잘못된 요청 데이터입니다."),
			@ApiResponse(responseCode = "409", description = "별칭이 이미 존재합니다.")
		}
	)
	@PostMapping
	@AuthorizeRole({"MEMBER"})
	public ResponseEntity<RegisterAddressResponse> registerAddress(
		@CurrentUser CurrentUserDetails currentUser, @Valid @RequestBody RegisterAddressRequest registerAddressRequest
	) {
		RegisterAddressResponse registerAddressResponse
			= addressService.registerAddress(currentUser, registerAddressRequest);
		return ResponseEntity.status(HttpStatus.CREATED).body(registerAddressResponse);
	}

	/**
	 * 등록된 주소 목록을 조회합니다.
	 *
	 * @param currentUser 현재 로그인된 사용자 정보
	 * @return 등록된 주소의 목록
	 */
	@Operation(
		summary = "주소 목록 조회",
		description = "등록된 주소 목록을 조회합니다.",
		responses = {
			@ApiResponse(responseCode = "200", description = "주소 목록이 성공적으로 반환되었습니다."),
			@ApiResponse(responseCode = "404", description = "주소를 찾을 수 없습니다.")
		}
	)
	@GetMapping
	@AuthorizeRole({"MEMBER"})
	public ResponseEntity<List<GetAddressResponse>> getAddresses(@CurrentUser CurrentUserDetails currentUser) {
		List<GetAddressResponse> addresses = addressService.getAddresses(currentUser);
		return ResponseEntity.status(HttpStatus.OK).body(addresses);
	}

	/**
	 * 기본 주소를 조회합니다.
	 *
	 * @param currentUser 현재 로그인된 사용자 정보
	 * @return 기본 주소 정보 (존재하지 않을 경우 Optional.empty() 반환)
	 */
	@Operation(
		summary = "기본 주소 조회",
		description = "기본 주소를 조회합니다.",
		responses = {
			@ApiResponse(responseCode = "200", description = "기본 주소가 성공적으로 반환되었습니다."),
			@ApiResponse(responseCode = "404", description = "기본 주소를 찾을 수 없습니다.")
		}
	)
	@GetMapping("/default")
	public ResponseEntity<Optional<GetAddressResponse>> getDefaultAddress(@CurrentUser CurrentUserDetails currentUser) {
		Optional<GetAddressResponse> address = addressService.getDefaultAddress(currentUser);
		return ResponseEntity.status(HttpStatus.OK).body(address);
	}

	/**
	 * 주소 정보를 수정합니다.
	 *
	 * @param currentUser 현재 로그인된 사용자 정보
	 * @param addressId 수정할 주소의 ID
	 * @param updateAddressRequest 수정할 주소 정보
	 * @return 수정된 주소에 대한 응답
	 */
	@Operation(
		summary = "주소 수정",
		description = "주소 정보를 수정합니다.",
		responses = {
			@ApiResponse(responseCode = "200", description = "주소가 성공적으로 수정되었습니다."),
			@ApiResponse(responseCode = "400", description = "잘못된 요청 데이터입니다."),
			@ApiResponse(responseCode = "404", description = "주소를 찾을 수 없습니다."),
			@ApiResponse(responseCode = "409", description = "별칭이 이미 존재합니다.")
		}
	)
	@PutMapping("/{addressId}")
	@AuthorizeRole({"MEMBER"})
	public ResponseEntity<UpdateAddressResponse> updateAddress(
		@CurrentUser CurrentUserDetails currentUser,
		@PathVariable Long addressId,
		@Valid @RequestBody UpdateAddressRequest updateAddressRequest
	) {
		UpdateAddressResponse updateAddressResponse
			= addressService.updateAddress(currentUser, addressId, updateAddressRequest);
		return ResponseEntity.status(HttpStatus.OK).body(updateAddressResponse);
	}

	/**
	 * 주소를 삭제합니다.
	 *
	 * @param currentUser 현재 로그인된 사용자 정보
	 * @param addressId 삭제할 주소의 ID
	 * @return 응답 상태 코드 (204 No Content)
	 */
	@Operation(
		summary = "주소 삭제",
		description = "주소를 삭제합니다.",
		responses = {
			@ApiResponse(responseCode = "204", description = "주소가 성공적으로 삭제되었습니다."),
			@ApiResponse(responseCode = "404", description = "주소를 찾을 수 없습니다.")
		}
	)
	@DeleteMapping("/{addressId}")
	@AuthorizeRole({"MEMBER"})
	public ResponseEntity<Void> deleteAddress(
		@CurrentUser CurrentUserDetails currentUser, @PathVariable Long addressId
	) {
		addressService.deleteAddress(currentUser, addressId);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
	}

	/**
	 * 주소를 기본 주소로 설정합니다.
	 *
	 * @param currentUser 현재 로그인된 사용자 정보
	 * @param addressId 기본 주소로 설정할 주소의 ID
	 * @return 응답 상태 코드 (200 OK)
	 */
	@Operation(
		summary = "기본 주소 설정",
		description = "주소를 기본 주소로 설정합니다.",
		responses = {
			@ApiResponse(responseCode = "200", description = "주소가 기본 주소로 성공적으로 설정되었습니다."),
			@ApiResponse(responseCode = "404", description = "주소를 찾을 수 없습니다.")
		}
	)
	@PutMapping("/{addressId}/default")
	@AuthorizeRole({"MEMBER"})
	public ResponseEntity<Void> setDefaultAddress(
		@CurrentUser CurrentUserDetails currentUser, @PathVariable Long addressId
	) {
		addressService.setDefaultAddress(currentUser, addressId);
		return ResponseEntity.status(HttpStatus.OK).body(null);
	}
}
