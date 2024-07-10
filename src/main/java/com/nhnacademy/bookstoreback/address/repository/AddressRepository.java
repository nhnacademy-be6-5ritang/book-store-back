package com.nhnacademy.bookstoreback.address.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nhnacademy.bookstoreback.address.domain.entity.Address;

public interface AddressRepository extends JpaRepository<Address, Long> {
	List<Address> findAllByUserId(Long userId);
}
