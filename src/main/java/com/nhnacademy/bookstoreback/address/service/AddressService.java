package com.nhnacademy.bookstoreback.address.service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.nhnacademy.bookstoreback.address.domain.dto.request.RegisterAddressRequest;
import com.nhnacademy.bookstoreback.address.domain.dto.request.UpdateAddressRequest;
import com.nhnacademy.bookstoreback.address.domain.dto.response.GetAddressResponse;
import com.nhnacademy.bookstoreback.address.domain.dto.response.RegisterAddressResponse;
import com.nhnacademy.bookstoreback.address.domain.dto.response.UpdateAddressResponse;
import com.nhnacademy.bookstoreback.address.domain.entity.Address;
import com.nhnacademy.bookstoreback.address.exception.AddressLimitExceededException;
import com.nhnacademy.bookstoreback.address.exception.AddressNotFoundException;
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

		if (userAddresses.isEmpty()) {
			address.updateIsDefault(true);
		}

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

	public void deleteAddress(CurrentUserDetails currentUser, Long addressId) {
		Long userId = currentUser.getUserId();

		Address address = addressRepository.findByIdAndUserId(addressId, userId)
			.orElseThrow(() -> new AddressNotFoundException(addressId, userId));

		addressRepository.delete(address);
	}

	public UpdateAddressResponse updateAddress(CurrentUserDetails currentUser, Long addressId,
		UpdateAddressRequest updateAddressRequest) {
		Long userId = currentUser.getUserId();

		Address address = addressRepository.findByIdAndUserId(addressId, userId)
			.orElseThrow(() -> new AddressNotFoundException(addressId, userId));

		List<Address> otherAddresses = addressRepository.findAllByUserId(userId);

		for (Address otherAddress : otherAddresses) {
			if (!addressId.equals(otherAddress.getId())
				&& updateAddressRequest.alias().equals(otherAddress.getAlias())) {
				throw new AliasAlreadyExistsException(updateAddressRequest.alias());
			}
		}

		address.update(updateAddressRequest);
		Address updatedAddress = addressRepository.save(address);

		return UpdateAddressResponse.fromEntity(updatedAddress);
	}

	public void setDefaultAddress(CurrentUserDetails currentUser, Long addressId) {
		Long userId = currentUser.getUserId();

		Address address = addressRepository.findByIdAndUserId(addressId, userId)
			.orElseThrow(() -> new AddressNotFoundException(addressId, userId));

		List<Address> addresses = addressRepository.findAllByUserId(userId);

		for (Address otherAddress : addresses) {
			if (otherAddress.getIsDefault() && !otherAddress.getId().equals(addressId)) {
				otherAddress.updateIsDefault(false);
				addressRepository.save(otherAddress);
			}
		}

		address.updateIsDefault(true);
		addressRepository.save(address);
	}

	public Optional<GetAddressResponse> getDefaultAddress(CurrentUserDetails currentUser) {
		if (currentUser == null) {
			return Optional.empty();
		}
		Long userId = currentUser.getUserId();

		Address address = addressRepository.findByUserIdAndIsDefault(userId, true)
			.orElse(null);

		if (address == null) {
			return Optional.empty();
		} else {
			return Optional.of(GetAddressResponse.fromEntity(address));
		}
	}
}
