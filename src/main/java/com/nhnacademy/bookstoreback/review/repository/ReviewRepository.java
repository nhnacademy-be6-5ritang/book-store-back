package com.nhnacademy.bookstoreback.review.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.nhnacademy.bookstoreback.review.domain.entity.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {
	Page<Review> findAllByBookBookId(Long bookId, Pageable pageable);
}
