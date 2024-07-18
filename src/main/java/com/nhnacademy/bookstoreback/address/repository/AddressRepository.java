package com.nhnacademy.bookstoreback.address.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nhnacademy.bookstoreback.address.domain.entity.Address;

public interface AddressRepository extends JpaRepository<Address, Long> {
	List<Address> findAllByUserId(Long userId);

	Optional<Address> findByIdAndUserId(Long addressId, Long userId);

	Optional<Address> findByUserIdAndIsDefault(Long userId, boolean isDefault);
}
