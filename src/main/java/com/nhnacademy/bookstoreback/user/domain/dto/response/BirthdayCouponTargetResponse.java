package com.nhnacademy.bookstoreback.user.domain.dto.response;

import java.time.LocalDate;

public record BirthdayCouponTargetResponse(
	Long userId,
	LocalDate birth){

}