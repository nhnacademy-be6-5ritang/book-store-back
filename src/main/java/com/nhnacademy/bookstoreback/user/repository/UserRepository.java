package com.nhnacademy.bookstoreback.user.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.nhnacademy.bookstoreback.user.domain.entity.User;

/**
 * @author 김태환
 * 사용자 정보에 대한 데이터 액세스를 제공하는 레포지토리 인터페이스입니다.
 */
public interface UserRepository extends JpaRepository<User, Long>, CustomUserRepository {
	@EntityGraph(attributePaths = {"userRoles.role"})
	@Query("SELECT u FROM User u")
	Page<User> findAllWithRoles(Pageable pageable);

	/**
	 * 주어진 이메일 주소로 사용자 존재 여부를 확인합니다.
	 *
	 * @param email 확인할 사용자의 이메일 주소
	 * @return 주어진 이메일 주소에 해당하는 사용자가 존재하면 {@code true}, 그렇지 않으면 {@code false}
	 */
	boolean existsByEmail(String email);

	/**
	 * 주어진 이메일 주소를 가진 사용자를 조회합니다.
	 *
	 * @param userEmail 조회할 사용자의 이메일 주소
	 * @return 주어진 이메일 주소와 일치하는 {@link User} 객체
	 */
	User findByEmail(String userEmail);

	/**
	 * 주어진 SSO ID로 사용자를 조회합니다.
	 *
	 * @param paycoIdNo 조회할 사용자의 SSO ID
	 * @return 주어진 SSO ID와 일치하는 {@link User} 객체를 포함하는 {@link Optional}.
	 *         SSO ID가 존재하지 않을 경우 빈 {@link Optional}을 반환합니다.
	 */
	Optional<User> findBySsoId(String paycoIdNo);
}
