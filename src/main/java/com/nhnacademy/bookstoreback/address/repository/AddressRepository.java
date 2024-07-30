package com.nhnacademy.bookstoreback.address.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nhnacademy.bookstoreback.address.domain.entity.Address;

/**
 * @author 김태환
 * Address 엔티티를 관리하는 Spring Data JPA 리포지토리입니다.
 */
public interface AddressRepository extends JpaRepository<Address, Long> {
	/**
	 * 주어진 사용자 ID와 연관된 모든 주소를 조회합니다.
	 *
	 * @param userId 주소를 조회할 사용자의 ID
	 * @return 주어진 사용자 ID와 연관된 {@link Address} 객체의 목록
	 */
	List<Address> findAllByUserId(Long userId);

	/**
	 * 주어진 주소 ID와 사용자 ID에 해당하는 주소를 조회합니다.
	 *
	 * @param addressId 조회할 주소의 ID
	 * @param userId 주소를 조회할 사용자의 ID
	 * @return 주어진 주소 ID와 사용자 ID에 해당하는 {@link Address} 객체가 존재하면 {@link Optional}으로 반환하며,
	 *         존재하지 않으면 빈 {@link Optional}을 반환합니다.
	 */
	Optional<Address> findByIdAndUserId(Long addressId, Long userId);

	/**
	 * 주어진 사용자 ID와 기본 주소 여부에 해당하는 주소를 조회합니다.
	 *
	 * @param userId 조회할 사용자의 ID
	 * @param isDefault 기본 주소 여부
	 * @return 주어진 사용자 ID와 기본 주소 여부에 해당하는 {@link Address} 객체가 존재하면 {@link Optional}으로 반환하며,
	 *         존재하지 않으면 빈 {@link Optional}을 반환합니다.
	 */
	Optional<Address> findByUserIdAndIsDefault(Long userId, boolean isDefault);
}
