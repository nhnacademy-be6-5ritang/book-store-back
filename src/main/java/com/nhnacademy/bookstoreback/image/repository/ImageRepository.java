package com.nhnacademy.bookstoreback.image.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nhnacademy.bookstoreback.image.domain.entity.Image;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
	Optional<Image> findByImageName(String imageName);
}
