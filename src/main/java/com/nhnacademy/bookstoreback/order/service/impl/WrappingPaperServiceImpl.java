package com.nhnacademy.bookstoreback.order.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nhnacademy.bookstoreback.order.domain.dto.response.GetListWrappingResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.GetWrappingResponse;
import com.nhnacademy.bookstoreback.order.domain.entity.BookOrder;
import com.nhnacademy.bookstoreback.order.domain.entity.PaperType;
import com.nhnacademy.bookstoreback.order.domain.entity.WrappingPaper;
import com.nhnacademy.bookstoreback.order.repository.BookOrderRepository;
import com.nhnacademy.bookstoreback.order.repository.PaperTypeRepository;
import com.nhnacademy.bookstoreback.order.repository.WrappingPaperRepository;
import com.nhnacademy.bookstoreback.order.service.WrappingPaperService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional
public class WrappingPaperServiceImpl implements WrappingPaperService {

	private final WrappingPaperRepository wrappingPaperRepository;
	private final PaperTypeRepository paperTypeRepository;
	private final BookOrderRepository bookOrderRepository;

	@Override
	public GetWrappingResponse createWrappingPapers(Long paperId, Long bookOrderId, Integer quantity) {
		BookOrder bookOrder = null;
		if (bookOrderId != null) {
			bookOrder = bookOrderRepository.getReferenceById(bookOrderId);
		}
		PaperType paperType = paperTypeRepository.findById(paperId).orElse(null);
		if (quantity != null) {
			return GetWrappingResponse.from(
				wrappingPaperRepository.save(WrappingPaper.toEntity(bookOrder, paperType, quantity)));
		}
		return GetWrappingResponse.from(
			wrappingPaperRepository.save(WrappingPaper.toEntity(bookOrder, paperType, null)));
	}

	@Override
	public void deleteWrappingPapers(Long id) {
		wrappingPaperRepository.deleteById(id);
	}

	@Override
	public GetListWrappingResponse getWrappingPaperByOrderListId(Long id) {
		return GetListWrappingResponse.from(wrappingPaperRepository.findAllByBookOrder_OrderListId(id));
	}
}
