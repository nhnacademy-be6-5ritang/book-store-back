package com.nhnacademy.bookstoreback.book.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nhnacademy.bookstoreback.book.domain.entity.Publisher;

/**
 * @author 김기욱
 * @version 1.0
 */
@Repository
public interface PublisherRepository extends JpaRepository<Publisher, Long> {
	Optional<Publisher> findByPublisherName(String publisherName);
}