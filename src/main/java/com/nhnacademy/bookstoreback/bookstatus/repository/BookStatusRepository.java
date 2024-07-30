package com.nhnacademy.bookstoreback.bookstatus.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nhnacademy.bookstoreback.bookstatus.domain.entity.BookStatus;

/**
 * @author 김기욱, 이경헌
 * 도서 상태 정보를 관리하는 Spring Data JPA 레포지토리입니다.
 */
public interface BookStatusRepository extends JpaRepository<BookStatus, Long> {
	/**
	 * 주어진 도서 상태 이름으로 도서 상태가 존재하는지 확인합니다.
	 *
	 * @param bookStatusName 확인할 도서 상태의 이름
	 * @return 주어진 도서 상태 이름과 일치하는 도서 상태가 존재하면 {@code true}, 그렇지 않으면 {@code false}
	 */
	boolean existsByBookStatusName(String bookStatusName);

	/**
	 * 주어진 도서 상태 이름으로 도서 상태를 조회합니다.
	 *
	 * @param bookStatusName 조회할 도서 상태의 이름
	 * @return 주어진 도서 상태 이름과 일치하는 {@link BookStatus} 객체를 포함하는 {@link Optional}.
	 *         도서 상태가 존재하지 않을 경우, 빈 {@link Optional}이 반환됩니다.
	 */
	Optional<BookStatus> findByBookStatusName(String bookStatusName);
}
