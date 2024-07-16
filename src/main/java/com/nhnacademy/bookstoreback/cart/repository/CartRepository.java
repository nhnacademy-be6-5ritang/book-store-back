package com.nhnacademy.bookstoreback.cart.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nhnacademy.bookstoreback.cart.domain.entity.Cart;

/**
 * @author 이경헌
 * 장바구니 엔티티를 관리하는 Spring Data JPA Repository입니다.
 */
public interface CartRepository extends JpaRepository<Cart, String> {
	/**
	 * 주어진 사용자 ID에 해당하는 장바구니가 존재하는지 여부를 확인합니다.
	 *
	 * @param userId 사용자 ID
	 * @return 존재 여부 (true/false)
	 */
	boolean existsByUserId(Long userId);

	/**
	 * 주어진 사용자 ID에 해당하는 장바구니를 조회합니다.
	 *
	 * @param userId 사용자 ID
	 * @return 조회된 장바구니 정보 (Optional 객체)
	 */
	Optional<Cart> findByUserId(Long userId);
}
