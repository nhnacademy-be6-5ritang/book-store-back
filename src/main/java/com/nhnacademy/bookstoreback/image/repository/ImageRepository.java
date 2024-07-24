package com.nhnacademy.bookstoreback.image.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nhnacademy.bookstoreback.image.domain.entity.Image;

/**
 * @author 김기욱
 * 이미지 엔티티를 관리하는 리포지토리 인터페이스입니다.
 */
public interface ImageRepository extends JpaRepository<Image, Long> {
	/**
	 *  이미지 이름을 기준으로 Image 엔티티를 조회합니다.
	 * @param imageName 이미지 이름
	 * @return 주어진 이미지 이름과 일치하는 {@code Image} 엔티티를 담고 있는 {@code Optional} 객체
	 */
	Optional<Image> findByImageName(String imageName);
}
