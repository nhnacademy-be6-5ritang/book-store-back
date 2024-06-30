package com.nhnacademy.bookstoreback.delivery.service.impl;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
import com.nhnacademy.bookstoreback.deliverypolicy.domain.entity.DeliveryPolicy;
import com.nhnacademy.bookstoreback.deliverypolicy.repository.DeliveryPolicyRepository;
import com.nhnacademy.bookstoreback.deliverystatus.domain.entity.DeliveryStatus;
import com.nhnacademy.bookstoreback.deliverystatus.repository.DeliveryStatusRepository;
import com.nhnacademy.bookstoreback.global.exception.NotFoundException;
import com.nhnacademy.bookstoreback.global.exception.payload.ErrorStatus;
import com.nhnacademy.bookstoreback.order.domain.entity.Order;
import com.nhnacademy.bookstoreback.order.repository.OrderRepository;

import lombok.RequiredArgsConstructor;

/**
 * @author 이경헌
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
	private final DeliveryPolicyRepository deliveryPolicyRepository;
	private final String NOT_FOUND_MESSAGE_DELIVERY_STATUS = "존재하지 않는 배달 상태입니다.";
	private final String INIT_DELIVERY_STATUS = "발송준비중";

	/**
	 * 사용자의 배송 목록을 페이지로 반환합니다.
	 *
	 * @param request  사용자의 배송 요청 정보.
	 * @param pageable 페이지 정보.
	 * @return 페이지로 반환된 사용자의 배송 목록.
	 */
	@Override
	@Transactional(readOnly = true)
	public Page<GetDeliveryResponse> getDeliveriesByUserId(GetDeliveriesRequest request, Pageable pageable) {
		int page = pageable.getPageNumber() - 1;
		int pageSize = 10;

		return deliveryRepository.findAllByOrder_Cart_User_Id(request.userId(),
				PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "delivery_sender_date")))
			.map(GetDeliveryResponse::fromEntity);
	}

	/**
	 * 특정 배송 ID에 대한 배송 정보를 반환합니다.
	 *
	 * @param deliveryId 배송 ID.
	 * @return 배송 정보.
	 * @throws NotFoundException 배송이 존재하지 않는 경우 발생.
	 */
	@Override
	@Transactional(readOnly = true)
	public GetDeliveryResponse getDelivery(Long deliveryId) {
		Delivery delivery = deliveryRepository.findById(deliveryId).orElseThrow(() -> {
			String errorMessage = String.format("해당 배송 '%s'은 존재하지 않는 배송입니다.", deliveryId);
			ErrorStatus errorStatus = ErrorStatus.from(errorMessage, HttpStatus.NOT_FOUND, LocalDateTime.now());
			return new NotFoundException(errorStatus);
		});

		return GetDeliveryResponse.fromEntity(delivery);
	}

	/**
	 * 새로운 배송을 생성합니다.
	 *
	 * @param request 배송 생성 요청 정보.
	 * @return 생성된 배송 정보.
	 * @throws NotFoundException 주문 또는 배송 상태가 존재하지 않는 경우 발생.
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

		DeliveryPolicy deliveryPolicy = deliveryPolicyRepository.findByDeliveryPolicyStandardPriceLessThanEqualOrderByDeliveryPolicyStandardPriceDesc(
			order.getOrderPrice());

		Delivery delivery = Delivery.toEntity(request, order, deliveryStatus, deliveryPolicy);

		deliveryRepository.save(delivery);

		return CreateDeliveryResponse.fromEntity(delivery);
	}

	/**
	 * 특정 배송 ID에 대한 배송 상태를 업데이트합니다.
	 *
	 * @param deliveryId 배송 ID.
	 * @param request    배송 업데이트 요청 정보.
	 * @return 업데이트된 배송 정보.
	 * @throws NotFoundException 배송 또는 주문 상태가 존재하지 않는 경우 발생.
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

		return UpdateDeliveryResponse.fromEntity(delivery);
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
