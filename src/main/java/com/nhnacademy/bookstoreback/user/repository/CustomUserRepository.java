package com.nhnacademy.bookstoreback.user.repository;

import java.time.LocalDate;
import java.util.List;

import com.nhnacademy.bookstoreback.user.domain.dto.response.BirthdayCouponTargetResponse;

/**
 *  @author 이기훈
 *
 * 생일쿠폰 발급시 생일 정보를 얻기위한 repository
 *
 */
public interface CustomUserRepository {
	List<BirthdayCouponTargetResponse> findByBirthDate(LocalDate date);
}
