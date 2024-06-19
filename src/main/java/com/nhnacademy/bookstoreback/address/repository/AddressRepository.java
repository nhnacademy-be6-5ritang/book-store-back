package com.nhnacademy.bookstoreback.address.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nhnacademy.bookstoreback.address.domain.entity.Address;

public interface AddressRepository extends JpaRepository<Address, Long> {
}
