package com.nhnacademy.bookstoreback.order.domain.dto.response;

import java.math.BigDecimal;
import java.util.List;

import lombok.Builder;


/**
 * 단건주문 bookId, categoryId 가져오는 dto
 * @author 이기훈
 */

@Builder
public record GetBookByOrderCouponResponse(
	Long bookId,
	BigDecimal bookPrice,
	List<Long>categoryId
) {

}
