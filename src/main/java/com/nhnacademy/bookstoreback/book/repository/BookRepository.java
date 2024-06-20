package com.nhnacademy.bookstoreback.book.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nhnacademy.bookstoreback.book.domain.entity.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
}
