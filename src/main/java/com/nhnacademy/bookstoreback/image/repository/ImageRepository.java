package com.nhnacademy.bookstoreback.image.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nhnacademy.bookstoreback.image.domain.entity.Image;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
