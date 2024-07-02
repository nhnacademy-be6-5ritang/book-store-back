package com.nhnacademy.bookstoreback.category.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nhnacademy.bookstoreback.category.domain.entity.BookCategory;

public interface BookCategoryRepository extends JpaRepository<BookCategory, Long> {
	void deleteAllByBookBookId(Long bookId);

	List<BookCategory> findAllByBookBookId(Long bookId);
}
