package com.nhnacademy.bookstoreback.deliverypolicy.controller;

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

import com.nhnacademy.bookstoreback.deliverypolicy.domain.dto.request.CreateDeliveryPolicyRequest;
import com.nhnacademy.bookstoreback.deliverypolicy.domain.dto.request.UpdateDeliveryPolicyRequest;
import com.nhnacademy.bookstoreback.deliverypolicy.domain.dto.response.CreateDeliveryPolicyResponse;
import com.nhnacademy.bookstoreback.deliverypolicy.domain.dto.response.GetDeliveryPoliciesResponse;
import com.nhnacademy.bookstoreback.deliverypolicy.domain.dto.response.GetDeliveryPolicyResponse;
import com.nhnacademy.bookstoreback.deliverypolicy.domain.dto.response.UpdateDeliveryPolicyResponse;
import com.nhnacademy.bookstoreback.deliverypolicy.service.DeliveryPolicyService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/deliveries-policies")
public class DeliveryPolicyController {
	private final DeliveryPolicyService deliveryPolicyService;

	@GetMapping
	public ResponseEntity<List<GetDeliveryPoliciesResponse>> getDeliveryPolicies() {
		return ResponseEntity.status(HttpStatus.OK).body(deliveryPolicyService.getDeliveryPolicies());
	}

	@GetMapping("/{deliveryPolicyId}")
	public ResponseEntity<GetDeliveryPolicyResponse> getDeliveryPolicy(@PathVariable Long deliveryPolicyId) {
		return ResponseEntity.status(HttpStatus.OK).body(deliveryPolicyService.getDeliveryPolicy(deliveryPolicyId));
	}

	@PostMapping
	public ResponseEntity<CreateDeliveryPolicyResponse> createDeliveryPolicy(
		@RequestBody CreateDeliveryPolicyRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(deliveryPolicyService.createDeliveryPolicy(request));
	}

	@PutMapping("/{deliveryPolicyId}")
	public ResponseEntity<UpdateDeliveryPolicyResponse> updateDeliveryPolicy(@PathVariable Long deliveryPolicyId,
		@RequestBody UpdateDeliveryPolicyRequest request) {
		return ResponseEntity.status(HttpStatus.OK)
			.body(deliveryPolicyService.updateDeliveryPolicy(deliveryPolicyId, request));
	}

	@DeleteMapping("/{deliveryPolicyId}")
	public ResponseEntity<Void> deleteDeliveryPolicy(@PathVariable Long deliveryPolicyId) {
		deliveryPolicyService.deleteDeliveryPolicy(deliveryPolicyId);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
}
