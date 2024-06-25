package com.nhnacademy.bookstoreback.delivery.service.impl;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nhnacademy.bookstoreback.delivery.domain.dto.request.CreateDeliveryRequest;
import com.nhnacademy.bookstoreback.delivery.domain.dto.request.GetDeliveriesRequest;
import com.nhnacademy.bookstoreback.delivery.domain.dto.request.UpdateDeliveryRequest;
import com.nhnacademy.bookstoreback.delivery.domain.dto.response.CreateDeliveryResponse;
import com.nhnacademy.bookstoreback.delivery.domain.dto.response.GetDeliveryResponse;
import com.nhnacademy.bookstoreback.delivery.domain.dto.response.UpdateDeliveryResponse;
import com.nhnacademy.bookstoreback.delivery.domain.entity.Delivery;
import com.nhnacademy.bookstoreback.delivery.repository.DeliveryRepository;
import com.nhnacademy.bookstoreback.delivery.service.DeliveryService;
import com.nhnacademy.bookstoreback.deliverystatus.domain.entity.DeliveryStatus;
import com.nhnacademy.bookstoreback.deliverystatus.repository.DeliveryStatusRepository;
import com.nhnacademy.bookstoreback.global.exception.NotFoundException;
import com.nhnacademy.bookstoreback.global.exception.payload.ErrorStatus;
import com.nhnacademy.bookstoreback.order.domain.entity.Order;
import com.nhnacademy.bookstoreback.order.repository.OrderRepository;

import lombok.RequiredArgsConstructor;

/**
 * 배달 서비스의 구현 클래스입니다.
 * 배달 생성, 조회, 업데이트, 삭제 기능을 제공합니다.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class DeliveryServiceImpl implements DeliveryService {
	private final DeliveryRepository deliveryRepository;
	private final OrderRepository orderRepository;
	private final DeliveryStatusRepository deliveryStatusRepository;
	private final String NOT_FOUND_MESSAGE_DELIVERY_STATUS = "존재하지 않는 배달 상태입니다.";
	private final String INIT_DELIVERY_STATUS = "발송준비중";

	/**
	 * 특정 사용자 ID와 연관된 배송 목록을 페이지 단위로 가져옵니다.
	 *
	 * @param request  사용자의 배송 조회 요청 정보를 포함하는 객체
	 * @param pageable 페이지네이션 정보로 페이지 크기, 정렬 등을 제어합니다.
	 * @return {@link GetDeliveryResponse} 객체들이 포함된 페이지. 각 배송의 세부 정보를 포함합니다.
	 */
	@Override
	@Transactional(readOnly = true)
	public Page<GetDeliveryResponse> getDeliveriesByUserId(GetDeliveriesRequest request, Pageable pageable) {
		return deliveryRepository.findAllByOrder_Cart_User_Id(request.userId(), pageable)
			.map(delivery -> GetDeliveryResponse.builder()
				.deliverySenderName(delivery.getDeliverySenderName())
				.deliverySenderPhone(delivery.getDeliverySenderPhone())
				.deliverySenderDate(delivery.getDeliverySenderDate())
				.deliverySenderAddress(delivery.getDeliverySenderAddress())
				.deliveryReceiver(delivery.getDeliveryReceiver())
				.deliveryReceiverPhone(delivery.getDeliveryReceiverPhone())
				.deliveryReceiverDate(delivery.getDeliveryReceiverDate())
				.deliveryReceiverAddress(delivery.getDeliveryReceiverAddress())
				.orderId(delivery.getOrder().getOrderId())
				.deliveryStatusName(delivery.getDeliveryStatus().getDeliveryStatusName())
				.build()
			);
	}

	/**
	 * 주어진 배송 ID에 해당하는 배송 정보를 조회합니다.
	 *
	 * @param deliveryId 조회할 배송의 ID
	 * @return 주어진 배송 ID에 해당하는 배송 정보를 포함하는 {@link GetDeliveryResponse} 객체
	 * @throws NotFoundException 주어진 ID에 해당하는 배송이 없을 경우 발생하는 예외
	 */
	@Override
	@Transactional(readOnly = true)
	public GetDeliveryResponse getDelivery(Long deliveryId) {
		Delivery delivery = deliveryRepository.findById(deliveryId).orElseThrow(() -> {
			String errorMessage = String.format("해당 배송 '%s'은 존재하지 않는 배송입니다.", deliveryId);
			ErrorStatus errorStatus = ErrorStatus.from(errorMessage, HttpStatus.NOT_FOUND, LocalDateTime.now());
			return new NotFoundException(errorStatus);
		});

		return GetDeliveryResponse.builder()
			.deliverySenderName(delivery.getDeliverySenderName())
			.deliverySenderPhone(delivery.getDeliverySenderPhone())
			.deliverySenderDate(delivery.getDeliverySenderDate())
			.deliverySenderAddress(delivery.getDeliverySenderAddress())
			.deliveryReceiver(delivery.getDeliveryReceiver())
			.deliveryReceiverPhone(delivery.getDeliveryReceiverPhone())
			.deliveryReceiverDate(delivery.getDeliveryReceiverDate())
			.deliveryReceiverAddress(delivery.getDeliveryReceiverAddress())
			.orderId(delivery.getOrder().getOrderId())
			.deliveryStatusName(delivery.getDeliveryStatus().getDeliveryStatusName())
			.build();
	}

	/**
	 * 새로운 배달을 생성합니다.
	 *
	 * @param request 배달 생성 요청의 세부 사항을 포함하는 객체
	 * @return 생성된 배달 정보를 포함하는 {@link CreateDeliveryResponse} 객체
	 * @throws NotFoundException 주문 또는 배달 상태가 존재하지 않는 경우 예외를 발생시킵니다.
	 */
	@Override
	public CreateDeliveryResponse createDelivery(CreateDeliveryRequest request) {
		Order order = orderRepository.findById(request.orderId()).orElseThrow(() -> {
			String errorMessage = String.format("해당 주문 '%d'은 존재하지 않는 주문입니다.", request.orderId());
			ErrorStatus errorStatus = ErrorStatus.from(errorMessage, HttpStatus.NOT_FOUND, LocalDateTime.now());
			return new NotFoundException(errorStatus);
		});

		DeliveryStatus deliveryStatus = Optional.of(deliveryStatusRepository.findDeliveryStatusByDeliveryStatusName(
				INIT_DELIVERY_STATUS))
			.orElseThrow(
				() -> new NotFoundException(ErrorStatus.from(NOT_FOUND_MESSAGE_DELIVERY_STATUS, HttpStatus.NOT_FOUND,
					LocalDateTime.now())));

		Delivery delivery = Delivery.builder()
			.deliverySenderName(request.deliverySenderName())
			.deliverySenderPhone(request.deliverySenderPhone())
			.deliverySenderDate(request.deliverySenderDate())
			.deliverySenderAddress(request.deliverySenderAddress())
			.deliveryReceiver(request.deliveryReceiver())
			.deliveryReceiverPhone(request.deliveryReceiverPhone())
			.deliveryReceiverDate(request.deliveryReceiverDate())
			.deliveryReceiverAddress(request.deliveryReceiverAddress())
			.order(order)
			.deliveryStatus(deliveryStatus)
			.build();

		deliveryRepository.save(delivery);

		return CreateDeliveryResponse.builder()
			.deliveryId(delivery.getDeliveryId())
			.deliverySenderName(delivery.getDeliverySenderName())
			.deliverySenderPhone(delivery.getDeliverySenderPhone())
			.deliverySenderDate(delivery.getDeliverySenderDate())
			.deliverySenderAddress(delivery.getDeliverySenderAddress())
			.deliveryReceiver(delivery.getDeliveryReceiver())
			.deliveryReceiverPhone(delivery.getDeliveryReceiverPhone())
			.deliveryReceiverDate(delivery.getDeliveryReceiverDate())
			.deliveryReceiverAddress(delivery.getDeliveryReceiverAddress())
			.orderId(delivery.getOrder().getOrderId())
			.deliveryStatusId(delivery.getDeliveryStatus().getDeliveryStatusId())
			.build();
	}

	/**
	 * 지정된 배달 ID에 대한 배달 정보를 업데이트합니다.
	 *
	 * @param deliveryId 업데이트할 배달의 ID
	 * @param request 새로운 배달 정보를 포함하는 업데이트 요청의 세부 사항
	 * @return 업데이트된 배달 정보를 포함하는 {@link UpdateDeliveryResponse} 객체
	 * @throws NotFoundException 배달 또는 배달 상태가 존재하지 않는 경우 예외를 발생시킵니다.
	 */
	@Override
	public UpdateDeliveryResponse updateDelivery(Long deliveryId, UpdateDeliveryRequest request) {
		Delivery delivery = deliveryRepository.findById(deliveryId).orElseThrow(() -> {
			String errorMessage = String.format("해당 배송 '%s'은 존재하지 않는 배송입니다.", deliveryId);
			ErrorStatus errorStatus = ErrorStatus.from(errorMessage, HttpStatus.NOT_FOUND, LocalDateTime.now());
			return new NotFoundException(errorStatus);
		});

		DeliveryStatus deliveryStatus = deliveryStatusRepository.findById(request.deliveryStatusId())
			.orElseThrow(() -> {
				String errorMessage = String.format("해당 주문상태 '%d'은 존재하지 않는 주문상태 입니다.", request.deliveryStatusId());
				ErrorStatus errorStatus = ErrorStatus.from(errorMessage, HttpStatus.NOT_FOUND, LocalDateTime.now());
				return new NotFoundException(errorStatus);
			});

		delivery.updateDeliveryStatus(deliveryStatus);
		deliveryRepository.save(delivery);

		return UpdateDeliveryResponse.builder()
			.deliveryId(delivery.getDeliveryId())
			.orderId(delivery.getOrder().getOrderId())
			.deliveryStatusName(delivery.getDeliveryStatus().getDeliveryStatusName())
			.build();
	}

	/**
	 * 지정된 배달 ID에 대한 배달 정보를 삭제합니다.
	 *
	 * @param deliveryId 삭제할 배달의 ID
	 */
	@Override
	public void deleteDelivery(Long deliveryId) {
		deliveryRepository.deleteById(deliveryId);
	}
}
