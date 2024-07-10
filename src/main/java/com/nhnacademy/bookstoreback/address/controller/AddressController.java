package com.nhnacademy.bookstoreback.address.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nhnacademy.bookstoreback.address.domain.dto.request.RegisterAddressRequest;
import com.nhnacademy.bookstoreback.address.domain.dto.response.GetAddressResponse;
import com.nhnacademy.bookstoreback.address.domain.dto.response.RegisterAddressResponse;
import com.nhnacademy.bookstoreback.address.service.AddressService;
import com.nhnacademy.bookstoreback.auth.annotation.CurrentUser;
import com.nhnacademy.bookstoreback.auth.jwt.dto.CurrentUserDetails;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/addresses")
public class AddressController {
	private final AddressService addressService;

	@PostMapping
	public ResponseEntity<RegisterAddressResponse> registerAddress(
		@CurrentUser CurrentUserDetails currentUser, @RequestBody RegisterAddressRequest registerAddressRequest
	) {
		RegisterAddressResponse registerAddressResponse
			= addressService.registerAddress(currentUser, registerAddressRequest);
		return ResponseEntity.status(HttpStatus.CREATED).body(registerAddressResponse);
	}

	@GetMapping
	public ResponseEntity<List<GetAddressResponse>> getAddresses(@CurrentUser CurrentUserDetails currentUser) {
		List<GetAddressResponse> addresses = addressService.getAddresses(currentUser);
		return ResponseEntity.status(HttpStatus.OK).body(addresses);
	}
}
