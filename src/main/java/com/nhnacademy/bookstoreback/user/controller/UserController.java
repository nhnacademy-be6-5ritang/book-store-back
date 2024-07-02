package com.nhnacademy.bookstoreback.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nhnacademy.bookstoreback.auth.annotation.CurrentUser;
import com.nhnacademy.bookstoreback.auth.jwt.dto.CurrentUserDetails;
import com.nhnacademy.bookstoreback.user.domain.dto.request.CreateUserRequest;
import com.nhnacademy.bookstoreback.user.domain.dto.request.UpdateUserInfoRequest;
import com.nhnacademy.bookstoreback.user.domain.dto.response.CreateUserResponse;
import com.nhnacademy.bookstoreback.user.domain.dto.response.GetMyUserInfoResponse;
import com.nhnacademy.bookstoreback.user.domain.dto.response.UpdateUserInfoResponse;
import com.nhnacademy.bookstoreback.user.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
	private final UserService userService;
	// private final AddressService addressService;

	@PostMapping
	public ResponseEntity<CreateUserResponse> signUpUser(@RequestBody CreateUserRequest createUserRequest) {
		CreateUserResponse createUserResponse = userService.createUser(createUserRequest);
		return ResponseEntity.status(HttpStatus.CREATED).body(createUserResponse);
	}

	@GetMapping("/self")
	public ResponseEntity<GetMyUserInfoResponse> getMyUserInfo(@CurrentUser CurrentUserDetails currentUser) {
		GetMyUserInfoResponse getMyUserInfoResponse = userService.getMyUserInfo(currentUser);
		return ResponseEntity.status(HttpStatus.OK).body(getMyUserInfoResponse);
	}

	@PutMapping
	public ResponseEntity<UpdateUserInfoResponse> updateUser(
		@CurrentUser CurrentUserDetails currentUser, @RequestBody UpdateUserInfoRequest updateUserInfoRequest
	) {
		UpdateUserInfoResponse updateUserInfoResponse = userService.updateUserInfo(currentUser, updateUserInfoRequest);
		return ResponseEntity.status(HttpStatus.OK).body(updateUserInfoResponse);
	}

	// 주소
	// @PostMapping("/addresses")
	// public ResponseEntity<RegisterAddressResponse> registerAddress(
	// 	@RequestBody RegisterAddressRequest registerAddressRequest) {
	// 	RegisterAddressResponse registerAddressResponse = addressService.registerAddress(registerAddressRequest);
	// 	return ResponseEntity.status(HttpStatus.CREATED).body(registerAddressResponse);
	// }

}
