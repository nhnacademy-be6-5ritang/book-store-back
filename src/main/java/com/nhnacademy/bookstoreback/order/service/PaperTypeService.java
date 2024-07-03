package com.nhnacademy.bookstoreback.order.service;

import com.nhnacademy.bookstoreback.order.domain.dto.request.CreateWrappingTypeRequest;
import com.nhnacademy.bookstoreback.order.domain.dto.request.UpdateWrappingTypeRequest;
import com.nhnacademy.bookstoreback.order.domain.dto.response.CreatePaperResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.GetAllPaperResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.GetPaperResponse;

public interface PaperTypeService {
	CreatePaperResponse createPaper(CreateWrappingTypeRequest createWrappingTypeRequest);

	GetAllPaperResponse getAllPaperTypes();

	GetPaperResponse getPaperTypeById(Long id);

	GetPaperResponse updatePaperTypeById(Long id, UpdateWrappingTypeRequest updateWrappingTypeRequest);

	void deletePaperTypeById(Long id);
}
