package com.nhnacademy.bookstoreback.author.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nhnacademy.bookstoreback.author.domain.entity.Author;

/**
 * 작가 Repository
 *
 * @author 김기욱
 * @version 1.0
 */
@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
	/**
	 * 작가 이름 기반 도서 조회
	 *
	 * @param authorName 작가 이름
	 * @return 작가
	 */
	Optional<Author> findByAuthorName(String authorName);

	boolean existsByAuthorName(String authorName);
}