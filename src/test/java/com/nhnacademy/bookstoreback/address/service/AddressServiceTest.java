package com.nhnacademy.bookstoreback.address.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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
import com.nhnacademy.bookstoreback.user.domain.dto.response.UserTokenInfo;
import com.nhnacademy.bookstoreback.user.domain.entity.User;
import com.nhnacademy.bookstoreback.user.exception.UserNotFoundException;
import com.nhnacademy.bookstoreback.user.repository.UserRepository;

class AddressServiceTest {

	@InjectMocks
	private AddressService addressService;

	@Mock
	private AddressRepository addressRepository;

	@Mock
	private UserRepository userRepository;

	private CurrentUserDetails currentUserDetails;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		UserTokenInfo userTokenInfo = UserTokenInfo.builder()
			.id(1L)
			.password("password")
			.roles(List.of("MEMBER"))
			.status("ACTIVE")
			.build();
		currentUserDetails = new CurrentUserDetails(userTokenInfo);
	}

	@Test
	void registerAddress_success() {
		RegisterAddressRequest request = RegisterAddressRequest.builder()
			.alias("Home")
			.postCode("10001")
			.baseAddress("123 Main St")
			.detailAddress("Apt 1")
			.build();

		User user = User.builder()
			.id(1L)
			.build();

		Address address = Address.builder()
			.id(1L)
			.user(user)
			.postCode("10001")
			.base("123 Main St")
			.detail("Apt 1")
			.alias("Home")
			.isDefault(true)
			.build();

		when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
		when(addressRepository.findAllByUserId(anyLong())).thenReturn(Collections.emptyList());
		when(addressRepository.save(any(Address.class))).thenReturn(address);

		RegisterAddressResponse response = addressService.registerAddress(currentUserDetails, request);

		assertNotNull(response);
		assertEquals(1L, response.id());
		assertEquals("Home", response.alias());
	}

	@Test
	void registerAddress_userNotFound() {
		RegisterAddressRequest request = RegisterAddressRequest.builder()
			.alias("Home")
			.postCode("10001")
			.baseAddress("123 Main St")
			.detailAddress("Apt 1")
			.build();

		when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

		assertThrows(UserNotFoundException.class, () -> addressService.registerAddress(currentUserDetails, request));
	}

	@Test
	void registerAddress_addressLimitExceeded() {
		RegisterAddressRequest request = RegisterAddressRequest.builder()
			.alias("Home")
			.postCode("10001")
			.baseAddress("123 Main St")
			.detailAddress("Apt 1")
			.build();

		User user = User.builder()
			.id(1L)
			.build();

		List<Address> addresses = Arrays.asList(new Address[10]);
		when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
		when(addressRepository.findAllByUserId(anyLong())).thenReturn(addresses);

		assertThrows(AddressLimitExceededException.class,
			() -> addressService.registerAddress(currentUserDetails, request));
	}

	@Test
	void registerAddress_aliasAlreadyExists() {
		RegisterAddressRequest request = RegisterAddressRequest.builder()
			.alias("Home")
			.postCode("10001")
			.baseAddress("123 Main St")
			.detailAddress("Apt 1")
			.build();

		User user = User.builder()
			.id(1L)
			.build();

		Address existingAddress = Address.builder()
			.id(1L)
			.user(user)
			.postCode("10001")
			.base("123 Main St")
			.detail("Apt 1")
			.alias("Home")
			.isDefault(true)
			.build();

		List<Address> userAddresses = Collections.singletonList(existingAddress);

		when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
		when(addressRepository.findAllByUserId(anyLong())).thenReturn(userAddresses);

		assertThrows(AliasAlreadyExistsException.class,
			() -> addressService.registerAddress(currentUserDetails, request));
	}

	@Test
	void registerAddress_aliasNotExists() {
		RegisterAddressRequest request = RegisterAddressRequest.builder()
			.alias("Work")
			.postCode("10001")
			.baseAddress("123 Main St")
			.detailAddress("Apt 1")
			.build();

		User user = User.builder()
			.id(1L)
			.build();

		Address existingAddress = Address.builder()
			.id(1L)
			.user(user)
			.postCode("10002")
			.base("456 Elm St")
			.detail("Apt 2")
			.alias("Home")
			.isDefault(true)
			.build();

		List<Address> userAddresses = Collections.singletonList(existingAddress);

		Address newAddress = Address.toEntity(request, user);
		newAddress = Address.builder()
			.id(2L)
			.user(user)
			.postCode("10001")
			.base("123 Main St")
			.detail("Apt 1")
			.alias("Work")
			.isDefault(false)
			.build();

		when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
		when(addressRepository.findAllByUserId(anyLong())).thenReturn(userAddresses);
		when(addressRepository.save(any(Address.class))).thenReturn(newAddress);

		RegisterAddressResponse response = addressService.registerAddress(currentUserDetails, request);

		assertNotNull(response);
		assertEquals(2L, response.id());
		assertEquals("Work", response.alias());
	}

	@Test
	void getAddresses() {
		User user = User.builder()
			.id(1L)
			.build();

		Address address1 = Address.builder()
			.id(1L)
			.user(user)
			.postCode("10001")
			.base("123 Main St")
			.detail("Apt 1")
			.alias("Home")
			.isDefault(true)
			.build();

		Address address2 = Address.builder()
			.id(2L)
			.user(user)
			.postCode("90001")
			.base("456 Elm St")
			.detail("Apt 2")
			.alias("Office")
			.isDefault(false)
			.build();

		when(addressRepository.findAllByUserId(anyLong())).thenReturn(Arrays.asList(address1, address2));

		List<GetAddressResponse> responses = addressService.getAddresses(currentUserDetails);

		assertEquals(2, responses.size());
		assertEquals("Home", responses.getFirst().alias());
	}

	@Test
	void deleteAddress_success() {
		User user = User.builder()
			.id(1L)
			.build();

		Address address = Address.builder()
			.id(1L)
			.user(user)
			.postCode("10001")
			.base("123 Main St")
			.detail("Apt 1")
			.alias("Home")
			.isDefault(true)
			.build();

		when(addressRepository.findByIdAndUserId(anyLong(), anyLong())).thenReturn(Optional.of(address));
		doNothing().when(addressRepository).delete(any(Address.class));

		assertDoesNotThrow(() -> addressService.deleteAddress(currentUserDetails, 1L));
	}

	@Test
	void deleteAddress_addressNotFound() {
		when(addressRepository.findByIdAndUserId(anyLong(), anyLong())).thenReturn(Optional.empty());

		assertThrows(AddressNotFoundException.class, () -> addressService.deleteAddress(currentUserDetails, 1L));
	}

	@Test
	void updateAddress_success() {
		UpdateAddressRequest request = new UpdateAddressRequest("Home", "10001", "123 Main St", "Apt 1");

		User user = User.builder()
			.id(1L)
			.build();

		Address address = Address.builder()
			.id(1L)
			.user(user)
			.postCode("10001")
			.base("123 Main St")
			.detail("Apt 1")
			.alias("Home")
			.isDefault(true)
			.build();

		when(addressRepository.findByIdAndUserId(anyLong(), anyLong())).thenReturn(Optional.of(address));
		when(addressRepository.save(any(Address.class))).thenReturn(address);

		UpdateAddressResponse response = addressService.updateAddress(currentUserDetails, 1L, request);

		assertNotNull(response);
		assertEquals(1L, response.id());
		assertEquals("Home", response.alias());
	}

	@Test
	void updateAddress_aliasAlreadyExists() {
		UpdateAddressRequest request = new UpdateAddressRequest("Home", "10001", "123 Main St", "Apt 1");

		User user = User.builder()
			.id(1L)
			.build();

		Address address = Address.builder()
			.id(1L)
			.user(user)
			.postCode("10001")
			.base("123 Main St")
			.detail("Apt 1")
			.alias("Office")
			.isDefault(true)
			.build();

		Address otherAddress = Address.builder()
			.id(2L)
			.user(user)
			.postCode("10002")
			.base("456 Elm St")
			.detail("Apt 2")
			.alias("Home")
			.isDefault(false)
			.build();

		List<Address> userAddresses = Arrays.asList(address, otherAddress);

		when(addressRepository.findByIdAndUserId(anyLong(), anyLong())).thenReturn(Optional.of(address));
		when(addressRepository.findAllByUserId(anyLong())).thenReturn(userAddresses);

		assertThrows(AliasAlreadyExistsException.class,
			() -> addressService.updateAddress(currentUserDetails, 1L, request));
	}

	@Test
	void updateAddress_aliasNotExists() {
		UpdateAddressRequest request = new UpdateAddressRequest("Work", "10001", "123 Main St", "Apt 1");

		User user = User.builder()
			.id(1L)
			.build();

		Address address = Address.builder()
			.id(1L)
			.user(user)
			.postCode("10001")
			.base("123 Main St")
			.detail("Apt 1")
			.alias("Office")
			.isDefault(true)
			.build();

		Address otherAddress = Address.builder()
			.id(2L)
			.user(user)
			.postCode("10002")
			.base("456 Elm St")
			.detail("Apt 2")
			.alias("Home")
			.isDefault(false)
			.build();

		List<Address> userAddresses = Arrays.asList(address, otherAddress);

		Address updatedAddress = Address.builder()
			.id(1L)
			.user(user)
			.postCode("10001")
			.base("123 Main St")
			.detail("Apt 1")
			.alias("Work")
			.isDefault(true)
			.build();

		when(addressRepository.findByIdAndUserId(anyLong(), anyLong())).thenReturn(Optional.of(address));
		when(addressRepository.findAllByUserId(anyLong())).thenReturn(userAddresses);
		when(addressRepository.save(any(Address.class))).thenReturn(updatedAddress);

		UpdateAddressResponse response = addressService.updateAddress(currentUserDetails, 1L, request);

		assertNotNull(response);
		assertEquals(1L, response.id());
		assertEquals("Work", response.alias());
	}

	@Test
	void setDefaultAddress() {
		User user = User.builder()
			.id(1L)
			.build();

		Address address1 = Address.builder()
			.id(1L)
			.user(user)
			.postCode("10001")
			.base("123 Main St")
			.detail("Apt 1")
			.alias("Home")
			.isDefault(false)
			.build();

		Address address2 = Address.builder()
			.id(2L)
			.user(user)
			.postCode("90001")
			.base("456 Elm St")
			.detail("Apt 2")
			.alias("Office")
			.isDefault(true)
			.build();

		when(addressRepository.findByIdAndUserId(anyLong(), anyLong())).thenReturn(Optional.of(address1));
		when(addressRepository.findAllByUserId(anyLong())).thenReturn(Arrays.asList(address1, address2));
		when(addressRepository.save(any(Address.class))).thenReturn(address1);

		assertDoesNotThrow(() -> addressService.setDefaultAddress(currentUserDetails, 1L));
	}

	@Test
	void setDefaultAddress_conditionFalse() {
		User user = User.builder()
			.id(1L)
			.build();

		Address addressToUpdate = Address.builder()
			.id(1L)
			.user(user)
			.postCode("10001")
			.base("123 Main St")
			.detail("Apt 1")
			.alias("Home")
			.isDefault(false)
			.build();

		List<Address> userAddresses = Collections.singletonList(addressToUpdate);

		when(addressRepository.findByIdAndUserId(anyLong(), anyLong())).thenReturn(Optional.of(addressToUpdate));
		when(addressRepository.findAllByUserId(anyLong())).thenReturn(userAddresses);
		when(addressRepository.save(any(Address.class))).thenReturn(addressToUpdate);

		assertDoesNotThrow(() -> addressService.setDefaultAddress(currentUserDetails, 1L));

		// Verify the default address was set
		assertTrue(addressToUpdate.getIsDefault());
		verify(addressRepository, times(1)).save(addressToUpdate);
	}

	@Test
	void setDefaultAddress_otherAddressEquals_conditionFalse() {
		User user = User.builder()
			.id(1L)
			.build();

		// 기본 주소로 설정하려는 주소
		Address addressToSetDefault = Address.builder()
			.id(1L)
			.user(user)
			.postCode("10001")
			.base("123 Main St")
			.detail("Apt 1")
			.alias("Home")
			.isDefault(true)
			.build();

		// 사용자의 주소 목록에 기본 주소로 설정하려는 주소만 포함
		List<Address> userAddresses = List.of(addressToSetDefault);

		when(addressRepository.findByIdAndUserId(anyLong(), anyLong())).thenReturn(Optional.of(addressToSetDefault));
		when(addressRepository.findAllByUserId(anyLong())).thenReturn(userAddresses);
		when(addressRepository.save(any(Address.class))).thenReturn(addressToSetDefault);

		// 메서드 호출
		addressService.setDefaultAddress(currentUserDetails, 1L);

		// 기본 주소로 설정하려는 주소가 여전히 기본 주소인지 확인
		assertTrue(addressToSetDefault.getIsDefault());

		// 기본 주소로 설정하려는 주소를 저장하는 메서드가 한 번만 호출되었는지 확인
		verify(addressRepository, times(1)).save(addressToSetDefault);
	}

	@Test
	void getDefaultAddress() {
		User user = User.builder()
			.id(1L)
			.build();

		Address address = Address.builder()
			.id(1L)
			.user(user)
			.postCode("10001")
			.base("123 Main St")
			.detail("Apt 1")
			.alias("Home")
			.isDefault(true)
			.build();

		when(addressRepository.findByUserIdAndIsDefault(anyLong(), anyBoolean())).thenReturn(
			Optional.ofNullable(address));

		Optional<GetAddressResponse> response = addressService.getDefaultAddress(currentUserDetails);

		assertTrue(response.isPresent());
		assertEquals("Home", response.get().alias());
	}

	@Test
	void getDefaultAddress_noDefaultAddress() {
		User user = User.builder()
			.id(1L)
			.build();

		when(addressRepository.findByUserIdAndIsDefault(anyLong(), eq(true))).thenReturn(Optional.empty());

		Optional<GetAddressResponse> response = addressService.getDefaultAddress(currentUserDetails);

		assertTrue(response.isEmpty());
	}
}
