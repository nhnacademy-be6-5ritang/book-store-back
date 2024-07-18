package com.nhnacademy.bookstoreback.order.service;

import com.nhnacademy.bookstoreback.order.domain.dto.request.CreateWrappingTypeRequest;
import com.nhnacademy.bookstoreback.order.domain.dto.request.UpdateWrappingTypeRequest;
import com.nhnacademy.bookstoreback.order.domain.dto.response.CreatePaperResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.GetAdminAllPaperResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.GetAllPaperResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.GetPaperResponse;

public interface PaperTypeService {
	/**
	 * 포장지 만들기
	 * @param createWrappingTypeRequest 포장지 정보
	 * @return 포장지 데이터 전부
	 */
	CreatePaperResponse createPaper(CreateWrappingTypeRequest createWrappingTypeRequest);

	/**
	 * 모든 포장지 가져오기
	 * 페이징 처리 예정
	 * @return 모든 포장지 리턴
	 */
	GetAllPaperResponse getAllPaperTypes();

	/**
	 * 포장지 아이디로 포장지 가져오기
	 * @param id 포장지 아이디
	 * @return 포장지 정보
	 */
	GetPaperResponse getPaperTypeById(Long id);

	/**
	 * 포장지 업데이트
	 * @param id 포장지 아이디
	 * @param updateWrappingTypeRequest 포장지 정보
	 * @return 포장지 정보
	 */
	GetPaperResponse updatePaperTypeById(Long id, UpdateWrappingTypeRequest updateWrappingTypeRequest);

	/**
	 * 포장지 삭제
	 * @param id 포장지 아이디
	 */
	void deletePaperTypeById(Long id);

	/**
	 * 관리자용 포장지 전부 가져오기
	 * @return 포장지 전부
	 */
	GetAdminAllPaperResponse getAdminAllPaperTypes();
}
