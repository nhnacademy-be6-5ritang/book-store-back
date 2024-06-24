package com.nhnacademy.bookstoreback.book.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nhnacademy.bookstoreback.book.domain.entity.Author;

/**
 * @author 김기욱
 * @version 1.0
 */
@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
	Optional<Author> findByAuthorName(String authorName);
}