package com.nhnacademy.bookstoreback.order.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.nhnacademy.bookstoreback.global.exception.WrappingFailException;
import com.nhnacademy.bookstoreback.global.exception.payload.ErrorStatus;
import com.nhnacademy.bookstoreback.order.domain.dto.request.CreateWrappingRequest;
import com.nhnacademy.bookstoreback.order.domain.dto.request.UpdateWrappingRequest;
import com.nhnacademy.bookstoreback.order.domain.dto.response.GetWrappingResponse;
import com.nhnacademy.bookstoreback.order.domain.entity.WrappingPaper;
import com.nhnacademy.bookstoreback.order.repository.WrappingPaperRepository;

@Service
public class WrappingPaperService {
	@Autowired
	private WrappingPaperRepository wrappingPaperRepository;

	public GetWrappingResponse createWrappingPapers(CreateWrappingRequest createWrappingRequest) {

		return GetWrappingResponse.from(wrappingPaperRepository.save((WrappingPaper.toEntity(createWrappingRequest))));
	}

	public GetWrappingResponse getWrappingPapers(Long id) {
		WrappingPaper wrappingPaper = wrappingPaperRepository.findById(id).orElse(null);
		if (wrappingPaper == null) {
			String errorMessage = "포장지를 가져올 수 없습니다";
			ErrorStatus errorStatus = ErrorStatus.from(errorMessage, HttpStatus.NOT_FOUND, LocalDateTime.now());
			throw new WrappingFailException(errorStatus);
		}
		return GetWrappingResponse.from(wrappingPaper);
	}

	public GetWrappingResponse updateWrappingPapers(Long id, UpdateWrappingRequest updateWrappingRequest) {
		WrappingPaper wrappingPaper = wrappingPaperRepository.findById(id).orElse(null);
		if (wrappingPaper == null) {
			String errorMessage = "포장지를 가져올 수 없습니다";
			ErrorStatus errorStatus = ErrorStatus.from(errorMessage, HttpStatus.NOT_FOUND, LocalDateTime.now());
			throw new WrappingFailException(errorStatus);
		}
		wrappingPaper.setWrappingPaperContent(updateWrappingRequest.wrappingPaperContent());
		wrappingPaper.setWrappingPaperName(updateWrappingRequest.wrappingPaperName());
		wrappingPaper.setWrappingPaperPrice(updateWrappingRequest.wrappingPaperPrice());
		return GetWrappingResponse.from(wrappingPaperRepository.save(wrappingPaper));
	}

	public void deleteWrappingPapers(Long id) {
		wrappingPaperRepository.deleteById(id);
	}

	public List<GetWrappingResponse> getAllWrappingPapers() {
		List<WrappingPaper> wrappingPapers = wrappingPaperRepository.findAll();
		List<GetWrappingResponse> list = new ArrayList<>();
		for (WrappingPaper wrappingPaper : wrappingPapers) {
			GetWrappingResponse getWrappingResponse = GetWrappingResponse.from(wrappingPaper);
			list.add(getWrappingResponse);
		}
		return list;
	}
}
