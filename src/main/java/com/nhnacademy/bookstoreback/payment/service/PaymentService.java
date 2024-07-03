package com.nhnacademy.bookstoreback.payment.service;

import com.nhnacademy.bookstoreback.order.domain.dto.response.GetBookOrderByInfoIdResponse;
import com.nhnacademy.bookstoreback.order.domain.dto.response.GetOrderByInfoResponse;
import com.nhnacademy.bookstoreback.payment.dto.response.CancelResponse;
import com.nhnacademy.bookstoreback.payment.dto.response.PaymentSaveResponse;
import com.nhnacademy.bookstoreback.payment.dto.response.TransactionsResponse;
import com.nhnacademy.bookstoreback.payment.dto.response.UpdatePaymentResponse;

public interface PaymentService {
	PaymentSaveResponse savePaymentResponse(String paymentResponseJson);

	TransactionsResponse transactions(String paymentResponseJson);

	GetBookOrderByInfoIdResponse findByOrderInfoId(String orderInfoId);

	GetOrderByInfoResponse findByOrder(String orderInfoId);

	CancelResponse paymentFindByOrderInfoId(String orderInfoId);

	UpdatePaymentResponse updatePayment(String paymentResponseJson, Long paymentId);
}
