package com.nhnacademy.bookstoreback.tag.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nhnacademy.bookstoreback.tag.domain.entity.BookTag;

public interface BookTagRepository extends JpaRepository<BookTag, Long> {
}
