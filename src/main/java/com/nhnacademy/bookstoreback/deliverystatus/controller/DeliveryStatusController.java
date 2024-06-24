package com.nhnacademy.bookstoreback.deliverystatus.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nhnacademy.bookstoreback.deliverystatus.domain.dto.request.CreateDeliveryStatusRequest;
import com.nhnacademy.bookstoreback.deliverystatus.domain.dto.request.UpdateDeliveryStatusRequest;
import com.nhnacademy.bookstoreback.deliverystatus.domain.dto.response.CreateDeliveryStatusResponse;
import com.nhnacademy.bookstoreback.deliverystatus.domain.dto.response.GetDeliveryStatusResponse;
import com.nhnacademy.bookstoreback.deliverystatus.domain.dto.response.UpdateDeliveryStatusResponse;
import com.nhnacademy.bookstoreback.deliverystatus.service.DeliveryStatusService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/deliveries-statuses")
public class DeliveryStatusController {
	private final DeliveryStatusService deliveryStatusService;

	@GetMapping
	public ResponseEntity<List<GetDeliveryStatusResponse>> getDeliveryStatuses() {
		List<GetDeliveryStatusResponse> responses = deliveryStatusService.getDeliveryStatuses();
		return ResponseEntity.status(HttpStatus.OK).body(responses);
	}

	@GetMapping("/{statusId}")
	public ResponseEntity<GetDeliveryStatusResponse> getDeliveryStatus(@PathVariable Long statusId) {
		GetDeliveryStatusResponse response = deliveryStatusService.getDeliveryStatus(statusId);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@PostMapping
	public ResponseEntity<CreateDeliveryStatusResponse> createDeliveryStatus(
		@RequestBody CreateDeliveryStatusRequest request) {
		CreateDeliveryStatusResponse response = deliveryStatusService.createDeliveryStatus(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@PutMapping("/{statusId}")
	public ResponseEntity<UpdateDeliveryStatusResponse> updateDeliveryStatus(@PathVariable Long statusId,
		@RequestBody UpdateDeliveryStatusRequest request) {
		UpdateDeliveryStatusResponse response = deliveryStatusService.updateDeliveryStatus(statusId, request);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@DeleteMapping("/{statusId}")
	public ResponseEntity<Void> deleteDeliveryStatus(@PathVariable Long statusId) {
		deliveryStatusService.deleteDeliveryStatus(statusId);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
}
