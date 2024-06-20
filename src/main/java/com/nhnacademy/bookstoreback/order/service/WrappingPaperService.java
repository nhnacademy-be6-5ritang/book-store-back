package com.nhnacademy.bookstoreback.order.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nhnacademy.bookstoreback.global.exception.WrappingFailException;
import com.nhnacademy.bookstoreback.global.exception.payload.ErrorStatus;
import com.nhnacademy.bookstoreback.order.domain.dto.request.CreateWrappingRequest;
import com.nhnacademy.bookstoreback.order.domain.dto.request.UpdateWrappingRequest;
import com.nhnacademy.bookstoreback.order.domain.dto.response.GetWrappingResponse;
import com.nhnacademy.bookstoreback.order.domain.entity.Order;
import com.nhnacademy.bookstoreback.order.domain.entity.WrappingPaper;
import com.nhnacademy.bookstoreback.order.repository.OrderRepository;
import com.nhnacademy.bookstoreback.order.repository.WrappingPaperRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional
public class WrappingPaperService {

	private final WrappingPaperRepository wrappingPaperRepository;
	private final OrderRepository orderRepository;

	public static final String ERROR_WRAPPING_PAPER_NOT_FOUND = "포장지를 가져올 수 없습니다";

	public GetWrappingResponse createWrappingPapers(CreateWrappingRequest createWrappingRequest, Long orderId) {
		Order order = orderRepository.findById(orderId).orElse(null);
		return GetWrappingResponse.from(
			wrappingPaperRepository.save(WrappingPaper.toEntity(createWrappingRequest, order)));
	}

	@Transactional(readOnly = true)
	public GetWrappingResponse getWrappingPapers(Long id) {
		WrappingPaper wrappingPaper = wrappingPaperRepository.findById(id).orElse(null);
		if (wrappingPaper == null) {
			ErrorStatus errorStatus = ErrorStatus.from(ERROR_WRAPPING_PAPER_NOT_FOUND, HttpStatus.NOT_FOUND,
				LocalDateTime.now());
			throw new WrappingFailException(errorStatus);
		}
		return GetWrappingResponse.from(wrappingPaper);
	}

	public GetWrappingResponse updateWrappingPapers(Long id, UpdateWrappingRequest updateWrappingRequest) {
		WrappingPaper wrappingPaper = wrappingPaperRepository.findById(id).orElse(null);
		if (wrappingPaper == null) {
			ErrorStatus errorStatus = ErrorStatus.from(ERROR_WRAPPING_PAPER_NOT_FOUND, HttpStatus.NOT_FOUND,
				LocalDateTime.now());
			throw new WrappingFailException(errorStatus);
		}
		wrappingPaper.update(updateWrappingRequest.wrappingPaperName(),
			updateWrappingRequest.wrappingPaperContent(), updateWrappingRequest.wrappingPaperPrice());
		return GetWrappingResponse.from(wrappingPaperRepository.save(wrappingPaper));
	}

	public void deleteWrappingPapers(Long id) {
		wrappingPaperRepository.deleteById(id);
	}

	@Transactional(readOnly = true)
	public List<GetWrappingResponse> getAllWrappingPapers() {
		List<WrappingPaper> wrappingPapers = wrappingPaperRepository.findAll();
		List<GetWrappingResponse> list = new ArrayList<>();
		for (WrappingPaper wrappingPaper : wrappingPapers) {
			GetWrappingResponse getWrappingResponse = GetWrappingResponse.from(wrappingPaper);
			list.add(getWrappingResponse);
		}
		return list;
	}

	//주문에 적용된 포장지 확인
	@Transactional(readOnly = true)
	public GetWrappingResponse findOrder_OrderId(Long orderId) {
		return GetWrappingResponse.from(wrappingPaperRepository.findByOrder_OrderId(orderId));
	}
}
