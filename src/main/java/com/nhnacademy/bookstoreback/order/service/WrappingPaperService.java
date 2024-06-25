package com.nhnacademy.bookstoreback.order.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nhnacademy.bookstoreback.order.domain.dto.request.CreateWrappingRequest;
import com.nhnacademy.bookstoreback.order.domain.dto.response.GetWrappingResponse;
import com.nhnacademy.bookstoreback.order.domain.entity.Order;
import com.nhnacademy.bookstoreback.order.domain.entity.PaperType;
import com.nhnacademy.bookstoreback.order.domain.entity.WrappingPaper;
import com.nhnacademy.bookstoreback.order.repository.OrderRepository;
import com.nhnacademy.bookstoreback.order.repository.PaperTypeRepository;
import com.nhnacademy.bookstoreback.order.repository.WrappingPaperRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional
public class WrappingPaperService {

	private final WrappingPaperRepository wrappingPaperRepository;
	private final OrderRepository orderRepository;
	private final PaperTypeRepository paperTypeRepository;

	public GetWrappingResponse createWrappingPapers(CreateWrappingRequest createWrappingRequest, Long orderId) {
		Order order = orderRepository.findById(orderId).orElse(null);
		PaperType paperType = paperTypeRepository.findById(createWrappingRequest.paperTypeId()).orElse(null);
		return GetWrappingResponse.from(
			wrappingPaperRepository.save(WrappingPaper.toEntity(order, paperType)));
	}

	public void deleteWrappingPapers(Long id) {
		wrappingPaperRepository.deleteById(id);
	}

	//주문에 적용된 포장지 확인
	@Transactional(readOnly = true)
	public GetWrappingResponse findOrder_OrderId(Long orderId) {
		return GetWrappingResponse.from(wrappingPaperRepository.findByOrder_OrderId(orderId));
	}
}
