package com.nhnacademy.bookstoreback.delivery.domain.dto.response;

import com.nhnacademy.bookstoreback.order.domain.entity.Order;

import lombok.Builder;

@Builder
public record UpdateDeliveryResponse(Order order, String deliveryStatusName) {
}