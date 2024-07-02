package com.nhnacademy.bookstoreback.tag.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nhnacademy.bookstoreback.tag.domain.entity.Tag;

public interface TagRepository extends JpaRepository<Tag, Long> {
	boolean existsByTagName(String tagName);
}
