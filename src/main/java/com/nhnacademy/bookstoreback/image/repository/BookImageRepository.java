package com.nhnacademy.bookstoreback.image.repository;

import org.springframework.data.repository.CrudRepository;

import com.nhnacademy.bookstoreback.book.domain.entity.Book;
import com.nhnacademy.bookstoreback.book.domain.entity.BookImage;
import com.nhnacademy.bookstoreback.image.domain.entity.Image;

public interface BookImageRepository extends CrudRepository<BookImage, Long> {
	boolean existsByBookAndImage(Book book, Image image);

	void deleteAllByBookBookId(Long bookId);
}
