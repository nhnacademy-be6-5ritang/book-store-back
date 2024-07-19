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
	@AuthorizeRole({"MEMBER"})
	public ResponseEntity<List<GetAddressResponse>> getAddresses(@CurrentUser CurrentUserDetails currentUser) {
		List<GetAddressResponse> addresses = addressService.getAddresses(currentUser);
		return ResponseEntity.status(HttpStatus.OK).body(addresses);
	}

	@GetMapping("/default")
	public ResponseEntity<Optional<GetAddressResponse>> getDefaultAddress(@CurrentUser CurrentUserDetails currentUser) {
		Optional<GetAddressResponse> address = addressService.getDefaultAddress(currentUser);
		return ResponseEntity.status(HttpStatus.OK).body(address);
	}

	@PutMapping("/{addressId}")
	public ResponseEntity<UpdateAddressResponse> updateAddress(
		@CurrentUser CurrentUserDetails currentUser,
		@PathVariable Long addressId,
		@RequestBody UpdateAddressRequest updateAddressRequest
	) {
		UpdateAddressResponse updateAddressResponse
			= addressService.updateAddress(currentUser, addressId, updateAddressRequest);
		return ResponseEntity.status(HttpStatus.OK).body(updateAddressResponse);
	}

	@DeleteMapping("/{addressId}")
	public ResponseEntity<Void> deleteAddress(
		@CurrentUser CurrentUserDetails currentUser, @PathVariable Long addressId
	) {
		addressService.deleteAddress(currentUser, addressId);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
	}

	@PutMapping("/{addressId}/default")
	public ResponseEntity<Void> setDefaultAddress(
		@CurrentUser CurrentUserDetails currentUser, @PathVariable Long addressId
	) {
		addressService.setDefaultAddress(currentUser, addressId);
		return ResponseEntity.status(HttpStatus.OK).body(null);
	}
}
