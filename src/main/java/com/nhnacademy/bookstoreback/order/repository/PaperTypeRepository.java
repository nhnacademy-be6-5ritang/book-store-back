package com.nhnacademy.bookstoreback.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nhnacademy.bookstoreback.order.domain.entity.PaperType;

public interface PaperTypeRepository extends JpaRepository<PaperType, Long> {
}
