package com.nhnacademy.bookstoreback.address.service;

import org.springframework.stereotype.Service;

import com.nhnacademy.bookstoreback.address.repository.AddressRepository;
import com.nhnacademy.bookstoreback.user.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class AddressService {
	private final AddressRepository addressRepository;
	private final UserRepository userRepository;

	// public RegisterAddressResponse registerAddress(RegisterAddressRequest registerAddressRequest) {
	// User user = userRepository.findById(registerAddressRequest.userId()).orElseThrow(() -> {
	// 	String errorMessage = String.format("해당 사용자 '%s'는 존재하지 않는 사용자입니다.", registerAddressRequest.userId());
	// 	ErrorStatus errorStatus = ErrorStatus.from(errorMessage, HttpStatus.NOT_FOUND, LocalDateTime.now());
	// 	return new UserNotFoundException(errorStatus);
	// });
	//
	// Address address = Address.toEntity(registerAddressRequest, user);
	// Address savedAddress = addressRepository.save(address);
	//
	// return RegisterAddressResponse.fromEntity(savedAddress);
	// }
}
