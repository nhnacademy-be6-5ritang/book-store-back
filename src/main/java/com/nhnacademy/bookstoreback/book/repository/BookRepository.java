package com.nhnacademy.bookstoreback.book.repository;

import com.nhnacademy.bookstoreback.book.domain.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}
