package com.nhnacademy.bookstoreback.author.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nhnacademy.bookstoreback.author.domain.entity.Author;

/**
 * @author 김기욱, 이경헌
 * 작가 정보를 관리하는 Spring Data JPA 레포지토리입니다.
 */
public interface AuthorRepository extends JpaRepository<Author, Long> {
	/**
	 * 작가 이름 기반 도서 조회
	 *
	 * @param authorName 작가 이름
	 * @return 작가
	 */
	Optional<Author> findByAuthorName(String authorName);

	/**
	 * 주어진 작가 이름으로 작가가 존재하는지 확인합니다.
	 *
	 * @param authorName 확인할 작가의 이름
	 * @return 주어진 작가 이름과 일치하는 작가가 존재하면 {@code true}, 그렇지 않으면 {@code false}
	 */
	boolean existsByAuthorName(String authorName);
}
