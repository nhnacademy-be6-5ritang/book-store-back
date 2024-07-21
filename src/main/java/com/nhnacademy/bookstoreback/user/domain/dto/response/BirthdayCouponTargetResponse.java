package com.nhnacademy.bookstoreback.user.domain.dto.response;

import java.time.LocalDate;

/**
 * 유저생일 정보 리턴해주는 dto
 *
 * @author 이기훈
 */
public record BirthdayCouponTargetResponse(
	Long userId,
	LocalDate birth) {

}