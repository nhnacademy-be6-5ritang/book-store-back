package com.nhnacademy.bookstoreback.address.service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.nhnacademy.bookstoreback.address.domain.dto.request.RegisterAddressRequest;
import com.nhnacademy.bookstoreback.address.domain.dto.response.GetAddressResponse;
import com.nhnacademy.bookstoreback.address.domain.dto.response.RegisterAddressResponse;
import com.nhnacademy.bookstoreback.address.domain.entity.Address;
import com.nhnacademy.bookstoreback.address.exception.AddressLimitExceededException;
import com.nhnacademy.bookstoreback.address.exception.AliasAlreadyExistsException;
import com.nhnacademy.bookstoreback.address.repository.AddressRepository;
import com.nhnacademy.bookstoreback.auth.jwt.dto.CurrentUserDetails;
import com.nhnacademy.bookstoreback.user.domain.entity.User;
import com.nhnacademy.bookstoreback.user.exception.UserNotFoundException;
import com.nhnacademy.bookstoreback.user.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class AddressService {
	private final AddressRepository addressRepository;
	private final UserRepository userRepository;

	public RegisterAddressResponse registerAddress(
		CurrentUserDetails currentUser, RegisterAddressRequest registerAddressRequest
	) {
		User user = userRepository.findById(currentUser.getUserId())
			.orElseThrow(() -> new UserNotFoundException(currentUser.getUserId()));

		List<Address> userAddresses = addressRepository.findAllByUserId(currentUser.getUserId());

		if (userAddresses.size() == 10) {
			throw new AddressLimitExceededException();
		}

		for (Address address : userAddresses) {
			if (registerAddressRequest.alias().equals(address.getAlias())) {
				throw new AliasAlreadyExistsException(registerAddressRequest.alias());
			}
		}

		Address address = Address.toEntity(registerAddressRequest, user);
		Address savedAddress = addressRepository.save(address);

		return RegisterAddressResponse.fromEntity(savedAddress);
	}

	public List<GetAddressResponse> getAddresses(CurrentUserDetails currentUser) {
		List<Address> addresses = addressRepository.findAllByUserId(currentUser.getUserId());
		return addresses.stream()
			.sorted(Comparator.comparing(Address::getIsDefault).reversed()
				.thenComparing(Address::getId))
			.map(GetAddressResponse::fromEntity)
			.collect(Collectors.toList());
	}
}
