package com.nhnacademy.bookstoreback.order.service;

import com.nhnacademy.bookstoreback.order.domain.dto.response.GetListWrappingResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.GetWrappingResponse;

public interface WrappingPaperService {
	/**
	 * 주문 리스트 포장지 설정
	 * @param paperId 포장지 아이디
	 * @param bookOrderId 주문 리스트 아이디
	 * @param quantity 책 개수
	 * @return 저장한 해당 정보 리턴
	 */
	GetWrappingResponse createWrappingPapers(Long paperId, Long bookOrderId, Integer quantity);

	/**
	 * 주문리스트 포장지 설정 삭제
	 * @param id 주문리스트 포장지 아이디 삭제
	 */
	void deleteWrappingPapers(Long id);

	/**
	 * 주문리스트 아이디로 설정된 포장지 찾기
	 * @param id 주문리스트 아이디
	 * @return 주문 리스트 아이디를 가지고 있는 포장지 설정 전부 리턴
	 */
	GetListWrappingResponse getWrappingPaperByOrderListId(Long id);
}
