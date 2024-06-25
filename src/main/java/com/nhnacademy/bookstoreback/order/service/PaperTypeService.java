package com.nhnacademy.bookstoreback.order.service;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nhnacademy.bookstoreback.global.exception.PaperFailException;
import com.nhnacademy.bookstoreback.global.exception.payload.ErrorStatus;
import com.nhnacademy.bookstoreback.order.domain.dto.request.CreateWrappingTypeRequest;
import com.nhnacademy.bookstoreback.order.domain.dto.request.UpdateWrappingTypeRequest;
import com.nhnacademy.bookstoreback.order.domain.dto.response.CreatePaperResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.GetAllPaperResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.GetPaperResponse;
import com.nhnacademy.bookstoreback.order.domain.entity.PaperType;
import com.nhnacademy.bookstoreback.order.repository.PaperTypeRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional
public class PaperTypeService {
	private final PaperTypeRepository paperTypeRepository;

	public static final String ERROR_PAPER_EXITS = "포장지를 가져올 수 없습니다";

	public CreatePaperResponse createPaper(CreateWrappingTypeRequest createWrappingTypeRequest) {
		return CreatePaperResponse.from(paperTypeRepository.save(PaperType.toEntity(createWrappingTypeRequest)));
	}

	@Transactional(readOnly = true)
	public GetAllPaperResponse getAllPaperTypes() {
		return GetAllPaperResponse.from(paperTypeRepository.findAll());
	}

	@Transactional(readOnly = true)
	public GetPaperResponse getPaperTypeById(Long id) {
		PaperType paperType = paperTypeRepository.findById(id).orElse(null);
		if (paperType == null) {
			ErrorStatus errorStatus = ErrorStatus.from(ERROR_PAPER_EXITS, HttpStatus.NOT_FOUND, LocalDateTime.now());
			throw new PaperFailException(errorStatus);
		}
		return GetPaperResponse.from(paperType);
	}

	public GetPaperResponse updatePaperTypeById(Long id, UpdateWrappingTypeRequest updateWrappingTypeRequest) {
		PaperType paperType = paperTypeRepository.findById(id).orElse(null);
		if (paperType == null) {
			ErrorStatus errorStatus = ErrorStatus.from(ERROR_PAPER_EXITS, HttpStatus.NOT_FOUND, LocalDateTime.now());
			throw new PaperFailException(errorStatus);
		}
		paperType.update(updateWrappingTypeRequest.paperName(), updateWrappingTypeRequest.paperContent(),
			updateWrappingTypeRequest.paperPrice());
		return GetPaperResponse.from(paperType);
	}

	public void deletePaperTypeById(Long id) {
		paperTypeRepository.deleteById(id);
	}
}
