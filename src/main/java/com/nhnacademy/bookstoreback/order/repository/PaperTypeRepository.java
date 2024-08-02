package com.nhnacademy.bookstoreback.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nhnacademy.bookstoreback.order.domain.entity.PaperType;

/**
 * @author 김다운
 * {@link PaperType} 엔티티에 대한 데이터베이스 작업을 위한 JPA 리포지토리 인터페이스입니다.
 */
public interface PaperTypeRepository extends JpaRepository<PaperType, Long> {
}
