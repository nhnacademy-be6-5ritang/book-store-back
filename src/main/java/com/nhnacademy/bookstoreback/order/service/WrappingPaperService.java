package com.nhnacademy.bookstoreback.order.service;

import com.nhnacademy.bookstoreback.order.domain.dto.response.GetListWrappingResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.GetWrappingResponse;

public interface WrappingPaperService {
	GetWrappingResponse createWrappingPapers(Long paperId, Long bookOrderId, Integer quantity);

	void deleteWrappingPapers(Long id);

	GetListWrappingResponse getWrappingPaperByOrderListId(Long id);
}
