package com.nhnacademy.bookstoreback.address.service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.Cacheable;
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

/**
 * @author 김태환
 * 사용자 주소 관련 기능을 구현하는 서비스 클래스입니다.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class AddressService {
	private final AddressRepository addressRepository;
	private final UserRepository userRepository;

	/**
	 * 사용자의 새 주소를 등록합니다.
	 *
	 * @param currentUser 현재 사용자 정보를 담고 있는 {@link CurrentUserDetails} 객체
	 * @param registerAddressRequest 등록할 주소 정보를 담고 있는 {@link RegisterAddressRequest} 객체
	 * @return 등록된 주소에 대한 {@link RegisterAddressResponse} 객체
	 * @throws UserNotFoundException 사용자가 존재하지 않을 경우
	 * @throws AddressLimitExceededException 사용자의 주소가 최대 개수를 초과할 경우
	 * @throws AliasAlreadyExistsException 주소의 별칭이 이미 존재할 경우
	 */
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

	/**
	 * 현재 사용자의 모든 주소를 조회합니다.
	 *
	 * @param currentUser 현재 사용자 정보를 담고 있는 {@link CurrentUserDetails} 객체
	 * @return 현재 사용자의 주소 목록을 담고 있는 {@link List} of {@link GetAddressResponse} 객체
	 */
	public List<GetAddressResponse> getAddresses(CurrentUserDetails currentUser) {
		List<Address> addresses = addressRepository.findAllByUserId(currentUser.getUserId());
		return addresses.stream()
			.sorted(Comparator.comparing(Address::getIsDefault).reversed()
				.thenComparing(Address::getId))
			.map(GetAddressResponse::fromEntity)
			.collect(Collectors.toList());
	}

	/**
	 * 사용자의 특정 주소를 삭제합니다.
	 *
	 * @param currentUser 현재 사용자 정보를 담고 있는 {@link CurrentUserDetails} 객체
	 * @param addressId 삭제할 주소의 ID
	 * @throws AddressNotFoundException 주어진 ID와 사용자 ID에 해당하는 주소가 존재하지 않을 경우
	 */
	public void deleteAddress(CurrentUserDetails currentUser, Long addressId) {
		Long userId = currentUser.getUserId();

		Address address = addressRepository.findByIdAndUserId(addressId, userId)
			.orElseThrow(() -> new AddressNotFoundException(addressId, userId));

		addressRepository.delete(address);
	}

	/**
	 * 사용자의 특정 주소를 수정합니다.
	 *
	 * @param currentUser 현재 사용자 정보를 담고 있는 {@link CurrentUserDetails} 객체
	 * @param addressId 수정할 주소의 ID
	 * @param updateAddressRequest 수정할 주소 정보가 담긴 {@link UpdateAddressRequest} 객체
	 * @return 수정된 주소에 대한 {@link UpdateAddressResponse} 객체
	 * @throws AddressNotFoundException 주어진 ID와 사용자 ID에 해당하는 주소가 존재하지 않을 경우
	 * @throws AliasAlreadyExistsException 주소의 별칭이 이미 존재할 경우
	 */
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

	/**
	 * 사용자의 기본 주소를 설정합니다.
	 *
	 * @param currentUser 현재 사용자 정보를 담고 있는 {@link CurrentUserDetails} 객체
	 * @param addressId 기본 주소로 설정할 주소의 ID
	 * @throws AddressNotFoundException 주어진 ID와 사용자 ID에 해당하는 주소가 존재하지 않을 경우
	 */
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

	/**
	 * 현재 사용자의 기본 주소를 조회합니다.
	 *
	 * @param currentUser 현재 사용자 정보를 담고 있는 {@link CurrentUserDetails} 객체
	 * @return 현재 사용자의 기본 주소에 대한 {@link GetAddressResponse} 객체를 포함하는 {@link Optional}.
	 *         기본 주소가 없을 경우 빈 {@link Optional}을 반환합니다.
	 */
	@Cacheable(value = "defaultAddressCache", key = "#currentUser.getUserId()")
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
