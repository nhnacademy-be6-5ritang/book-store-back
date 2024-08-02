package com.nhnacademy.bookstoreback.image.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nhnacademy.bookstoreback.image.domain.entity.Image;

/**
 * @author 김기욱
 * 이미지 엔티티를 관리하는 리포지토리 인터페이스입니다.
 */
public interface ImageRepository extends JpaRepository<Image, Long> {
}
