package com.nhnacademy.bookstoreback.order.repository;

import com.nhnacademy.bookstoreback.order.domain.dto.response.GetBookByOrderCouponResponse;

public interface CustomBookOrderRepository {
    GetBookByOrderCouponResponse findBooksByOrderListId(Long orderListId);
}