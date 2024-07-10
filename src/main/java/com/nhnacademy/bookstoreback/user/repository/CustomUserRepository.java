package com.nhnacademy.bookstoreback.user.repository;

import java.util.List;

import com.nhnacademy.bookstoreback.user.domain.dto.response.BirthdayCouponTargetResponse;

/**
 * @author 이기훈
 * querydsl적용한 커스텀 레포지토리
 */
public interface CustomUserRepository {

	/**
	 * @author 이기훈
	 * @param month 유저 생일
	 * @param day 유저 생일
	 * @return 해당 날짜가 생일인 유저의 생일리스트를 리턴
	 */
	List<BirthdayCouponTargetResponse> findUsersWithBirthMonthDay(int month, int day);
}
