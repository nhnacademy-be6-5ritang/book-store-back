package com.nhnacademy.bookstoreback.image.repository;

import com.nhnacademy.bookstoreback.book.domain.entity.BookImage;
import com.nhnacademy.bookstoreback.book.domain.entity.Book;
import com.nhnacademy.bookstoreback.image.domain.entity.Image;
import org.springframework.data.repository.CrudRepository;

public interface BookImageRepository extends CrudRepository<BookImage, Long> {
	boolean existsByBookAndImage(Book book, Image image);
}
