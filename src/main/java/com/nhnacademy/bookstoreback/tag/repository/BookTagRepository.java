package com.nhnacademy.bookstoreback.tag.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nhnacademy.bookstoreback.tag.domain.entity.BookTag;

public interface BookTagRepository extends JpaRepository<BookTag, Long> {
	void deleteAllByBookBookId(Long bookId);

	List<BookTag> findAllByBookBookId(Long bookId);
}
